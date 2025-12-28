package com.blockchain.aigc.service;

import cn.hutool.core.util.IdUtil;
import com.blockchain.aigc.dto.LoginRequest;
import com.blockchain.aigc.dto.RegisterRequest;
import com.blockchain.aigc.dto.UserProfileDTO;
import com.blockchain.aigc.entity.User;
import com.blockchain.aigc.entity.UserRealname;
import com.blockchain.aigc.entity.UserWallet;
import com.blockchain.aigc.enums.ChainTypeEnum;
import com.blockchain.aigc.enums.UserAuthEnum;
import com.blockchain.aigc.enums.UserStatusEnum;
import com.blockchain.aigc.enums.VerifyStatusEnum;
import com.blockchain.aigc.mapper.UserMapper;
import com.blockchain.aigc.mapper.UserRealnameMapper;
import com.blockchain.aigc.mapper.UserWalletMapper;
import com.blockchain.aigc.utils.JwtUtil;
import com.blockchain.aigc.utils.UserUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.blockchain.aigc.entity.table.UserRealnameTableDef.USER_REALNAME;
import static com.blockchain.aigc.entity.table.UserTableDef.USER;
import static com.blockchain.aigc.entity.table.UserWalletTableDef.USER_WALLET;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserWalletMapper userWalletMapper;

    @Autowired
    private UserRealnameMapper userRealnameMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BlockchainService blockchainService;

    @Value("${contract.address-action}")
    private String contractAddress;

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> register(RegisterRequest request) {
        // 检查用户名是否存在
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(USER.USERNAME.eq(request.getUsername()));
        User existUser = userMapper.selectOneByQuery(queryWrapper);
        if (existUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes()));
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setAuthStatus(UserAuthEnum.INIT);  // 用户初始状态为未认证
        user.setStatus(UserStatusEnum.ENABLE);

        userMapper.insert(user);

        // 不再创建钱包地址，等待实名认证时再创建

        // 生成token (使用userId)
        String token = jwtUtil.generateToken(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("walletAddress", null);  // 注册时无钱包地址

        return result;
    }

    public Map<String, Object> login(LoginRequest request) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(USER.USERNAME.eq(request.getUsername()));
        User user = userMapper.selectOneByQuery(queryWrapper);

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        String encryptedPassword = DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
        if (!user.getPassword().equals(encryptedPassword)) {
            throw new RuntimeException("密码错误");
        }

        if (user.getStatus() == UserStatusEnum.DISABLE) {
            throw new RuntimeException("账号已被禁用");
        }

        // 获取钱包地址
        QueryWrapper walletQuery = QueryWrapper.create()
                .where(USER_WALLET.USER_ID.eq(user.getId()));
        UserWallet wallet = userWalletMapper.selectOneByQuery(walletQuery);

        // 生成token (使用userId)
        String token = jwtUtil.generateToken(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("walletAddress", wallet != null ? wallet.getWalletAddress() : null);

        return result;
    }

    public UserProfileDTO getProfile() {
        User user = UserUtil.get();
        if (user == null) {
            throw new RuntimeException("用户未登录");
        }

        // 获取钱包地址
        QueryWrapper walletQuery = QueryWrapper.create()
                .where(USER_WALLET.USER_ID.eq(user.getId()));
        UserWallet wallet = userWalletMapper.selectOneByQuery(walletQuery);

        UserProfileDTO dto = new UserProfileDTO();
        BeanUtils.copyProperties(user, dto);
        dto.setWalletAddress(wallet != null ? wallet.getWalletAddress() : null);

        return dto;
    }

    public User getUserByUsername(String username) {
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(USER.USERNAME.eq(username));
        return userMapper.selectOneByQuery(queryWrapper);
    }

    public User getUserById(Long userId) {
        return userMapper.selectOneById(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(UserProfileDTO request) {
        User currentUser = UserUtil.get();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }

        // 只能更新自己的信息
        if (!currentUser.getId().equals(request.getId())) {
            throw new RuntimeException("只能更新自己的信息");
        }

        // 检查用户名是否被占用（排除自己）
        if (request.getUsername() != null && !request.getUsername().equals(currentUser.getUsername())) {
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .where(USER.USERNAME.eq(request.getUsername()))
                    .and(USER.ID.ne(currentUser.getId()));
            User existUser = userMapper.selectOneByQuery(queryWrapper);
            if (existUser != null) {
                throw new RuntimeException("用户名已被占用");
            }
        }

        // 更新允许修改的字段：username、phone、email
        User updateUser = new User();
        updateUser.setId(currentUser.getId());

        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            updateUser.setUsername(request.getUsername());
        }
        if (request.getPhone() != null) {
            updateUser.setPhone(request.getPhone());
        }
        if (request.getEmail() != null) {
            updateUser.setEmail(request.getEmail());
        }

        // authStatus、status、walletAddress 不允许用户更新，忽略这些字段

        userMapper.update(updateUser);
    }

    @Transactional(rollbackFor = Exception.class)
    public void realnameAuth(String realName, String idNumber) {
        User currentUser = UserUtil.get();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }

        // 检查是否已经实名认证
        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(USER_REALNAME.USER_ID.eq(currentUser.getId()));
        UserRealname existingAuth = userRealnameMapper.selectOneByQuery(queryWrapper);

        if (existingAuth != null) {
            if (existingAuth.getVerifyStatus() == VerifyStatusEnum.PASSED) {
                throw new RuntimeException("用户已通过实名认证");
            } else if (existingAuth.getVerifyStatus() == VerifyStatusEnum.PENDING) {
                throw new RuntimeException("实名认证已在审核中");
            }
        }

        // 创建或更新实名认证记录
        UserRealname userRealname = (existingAuth != null) ? existingAuth : new UserRealname();
        userRealname.setUserId(currentUser.getId());
        userRealname.setRealName(realName);
        userRealname.setIdNumber(idNumber);
        userRealname.setVerifyStatus(VerifyStatusEnum.PENDING);

        if (existingAuth != null) {
            userRealnameMapper.update(userRealname);
        } else {
            userRealnameMapper.insert(userRealname);
        }

        // TODO: 调用第三方实名认证接口
        // 模拟认证通过，直接更新状态为通过
        userRealname.setVerifyStatus(VerifyStatusEnum.PASSED);
        userRealname.setVerifyTime(LocalDateTime.now());
        userRealnameMapper.update(userRealname);

        // 检查用户是否已有钱包，如果没有则创建
        QueryWrapper walletQuery = QueryWrapper.create()
                .where(USER_WALLET.USER_ID.eq(currentUser.getId()));
        UserWallet existingWallet = userWalletMapper.selectOneByQuery(walletQuery);

        CryptoKeyPair cryptoKeyPair = null;
        if (existingWallet == null) {
            // 创建钱包地址
            cryptoKeyPair = blockchainService.createCryptoKeyPair();
            String walletAddress = cryptoKeyPair.getAddress();
            UserWallet userWallet = new UserWallet();
            userWallet.setUserId(currentUser.getId());
            userWallet.setChainType(ChainTypeEnum.FISCO_BCOS);
            userWallet.setWalletAddress(walletAddress);
            userWalletMapper.insert(userWallet);
        }

        // 更新用户认证状态
        User updateUser = new User();
        updateUser.setId(currentUser.getId());
        updateUser.setAuthStatus(UserAuthEnum.AUTH);
        userMapper.update(updateUser);

        try {
            blockchainService.doAction("0xfa6ac41d0e064cfb50321de940cd7a7907b2dede", cryptoKeyPair);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserRealname getRealnameAuth() {
        User currentUser = UserUtil.get();
        if (currentUser == null) {
            throw new RuntimeException("用户未登录");
        }

        QueryWrapper queryWrapper = QueryWrapper.create()
                .where(USER_REALNAME.USER_ID.eq(currentUser.getId()));
        return userRealnameMapper.selectOneByQuery(queryWrapper);
    }
}
