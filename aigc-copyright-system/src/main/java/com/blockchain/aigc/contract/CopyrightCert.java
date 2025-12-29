package com.blockchain.aigc.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class CopyrightCert extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610859806100206000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063573d21f31461005c5780636052d31f1461013c5780636b188288146101cf575b600080fd5b34801561006857600080fd5b50610087600480360381019080803590602001909291905050506101fc565b60405180806020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828103825285818151815260200191508051906020019080838360005b838110156100ff5780820151818401526020810190506100e4565b50505050905090810190601f16801561012c5780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b34801561014857600080fd5b506101cd60048036038101908080359060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506103df565b005b3480156101db57600080fd5b506101fa6004803603810190808035906020019092919050505061055b565b005b6060600080610209610747565b60008086815260200190815260200160002060806040519081016040529081600082018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102c05780601f10610295576101008083540402835291602001916102c0565b820191906000526020600020905b8154815290600101906020018083116102a357829003601f168201915b505050505081526020016001820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001600282015481526020016003820160009054906101000a900460ff1615151515815250509050806060015115156103bf576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600f8152602001807f776f726b206e6f7420657869737473000000000000000000000000000000000081525060200191505060405180910390fd5b806000015181602001518260400151829250935093509350509193909250565b60008084815260200190815260200160002060030160009054906101000a900460ff16151515610477576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f776f726b20616c7265616479206578697374730000000000000000000000000081525060200191505060405180910390fd5b6080604051908101604052808381526020018273ffffffffffffffffffffffffffffffffffffffff168152602001600081526020016001151581525060008085815260200190815260200160002060008201518160000190805190602001906104e1929190610788565b5060208201518160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506040820151816002015560608201518160030160006101000a81548160ff021916908315150217905550905050505050565b600080600083815260200190815260200160002090508060030160009054906101000a900460ff1615156105f7576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600f8152602001807f776f726b206e6f7420657869737473000000000000000000000000000000000081525060200191505060405180910390fd5b8060010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156106be576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260178152602001807f6f6e6c7920617574686f722063616e20636f6e6669726d00000000000000000081525060200191505060405180910390fd5b6000816002015414151561073a576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f616c726561647920636f6e6669726d656400000000000000000000000000000081525060200191505060405180910390fd5b4281600201819055505050565b60806040519081016040528060608152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600081526020016000151581525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106107c957805160ff19168380011785556107f7565b828001600101855582156107f7579182015b828111156107f65782518255916020019190600101906107db565b5b5090506108049190610808565b5090565b61082a91905b8082111561082657600081600090555060010161080e565b5090565b905600a165627a7a72305820988dc44e25193bec0751da485b3a34aa9d6d8c083eaa805ec6173f67744a67730029"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50610859806100206000396000f300608060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680631f3f4dd21461005c578063b0517bf414610089578063e25601e11461011c575b600080fd5b34801561006857600080fd5b50610087600480360381019080803590602001909291905050506101fc565b005b34801561009557600080fd5b5061011a60048036038101908080359060200190929190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506103e8565b005b34801561012857600080fd5b5061014760048036038101908080359060200190929190505050610564565b60405180806020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828103825285818151815260200191508051906020019080838360005b838110156101bf5780820151818401526020810190506101a4565b50505050905090810190601f1680156101ec5780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b600080600083815260200190815260200160002090508060030160009054906101000a900460ff161515610298576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252600f8152602001807f776f726b206e6f7420657869737473000000000000000000000000000000000081525060200191505060405180910390fd5b8060010160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561035f576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260178152602001807f6f6e6c7920617574686f722063616e20636f6e6669726d00000000000000000081525060200191505060405180910390fd5b600081600201541415156103db576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260118152602001807f616c726561647920636f6e6669726d656400000000000000000000000000000081525060200191505060405180910390fd5b4281600201819055505050565b60008084815260200190815260200160002060030160009054906101000a900460ff16151515610480576040517fc703cb120000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f776f726b20616c7265616479206578697374730000000000000000000000000081525060200191505060405180910390fd5b6080604051908101604052808381526020018273ffffffffffffffffffffffffffffffffffffffff168152602001600081526020016001151581525060008085815260200190815260200160002060008201518160000190805190602001906104ea929190610747565b5060208201518160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506040820151816002015560608201518160030160006101000a81548160ff021916908315150217905550905050505050565b60606000806105716107c7565b60008086815260200190815260200160002060806040519081016040529081600082018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106285780601f106105fd57610100808354040283529160200191610628565b820191906000526020600020905b81548152906001019060200180831161060b57829003601f168201915b505050505081526020016001820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001600282015481526020016003820160009054906101000a900460ff161515151581525050905080606001511515610727576040517fc703cb1200000000000000000000000000000000000000000000000000000000815260040180806020018281038252600f8152602001807f776f726b206e6f7420657869737473000000000000000000000000000000000081525060200191505060405180910390fd5b806000015181602001518260400151829250935093509350509193909250565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061078857805160ff19168380011785556107b6565b828001600101855582156107b6579182015b828111156107b557825182559160200191906001019061079a565b5b5090506107c39190610808565b5090565b60806040519081016040528060608152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600081526020016000151581525090565b61082a91905b8082111561082657600081600090555060010161080e565b5090565b905600a165627a7a72305820c546a3f49bc890b75e30e680ed7b8d0f558f27c685c32c967c2cc723b7a7535a0029"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":true,\"inputs\":[{\"name\":\"workId\",\"type\":\"uint256\"}],\"name\":\"getWork\",\"outputs\":[{\"name\":\"fileHash\",\"type\":\"string\"},{\"name\":\"authorAddress\",\"type\":\"address\"},{\"name\":\"certifyTime\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"workId\",\"type\":\"uint256\"},{\"name\":\"fileHash\",\"type\":\"string\"},{\"name\":\"author\",\"type\":\"address\"}],\"name\":\"registerWork\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"workId\",\"type\":\"uint256\"}],\"name\":\"confirmCopyright\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_GETWORK = "getWork";

    public static final String FUNC_REGISTERWORK = "registerWork";

    public static final String FUNC_CONFIRMCOPYRIGHT = "confirmCopyright";

    protected CopyrightCert(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public Tuple3<String, String, BigInteger> getWork(BigInteger workId) throws ContractException {
        final Function function = new Function(FUNC_GETWORK,
                Arrays.<Type>asList(new Uint256(workId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple3<String, String, BigInteger>(
                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (BigInteger) results.get(2).getValue());
    }

    public TransactionReceipt registerWork(BigInteger workId, String fileHash, String author) {
        final Function function = new Function(
                FUNC_REGISTERWORK,
                Arrays.<Type>asList(new Uint256(workId),
                new Utf8String(fileHash),
                new Address(author)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] registerWork(BigInteger workId, String fileHash, String author, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_REGISTERWORK,
                Arrays.<Type>asList(new Uint256(workId),
                new Utf8String(fileHash),
                new Address(author)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForRegisterWork(BigInteger workId, String fileHash, String author) {
        final Function function = new Function(
                FUNC_REGISTERWORK,
                Arrays.<Type>asList(new Uint256(workId),
                new Utf8String(fileHash),
                new Address(author)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple3<BigInteger, String, String> getRegisterWorkInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_REGISTERWORK,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<BigInteger, String, String>(

                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue()
                );
    }

    public TransactionReceipt confirmCopyright(BigInteger workId) {
        final Function function = new Function(
                FUNC_CONFIRMCOPYRIGHT,
                Arrays.<Type>asList(new Uint256(workId)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] confirmCopyright(BigInteger workId, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CONFIRMCOPYRIGHT,
                Arrays.<Type>asList(new Uint256(workId)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForConfirmCopyright(BigInteger workId) {
        final Function function = new Function(
                FUNC_CONFIRMCOPYRIGHT,
                Arrays.<Type>asList(new Uint256(workId)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<BigInteger> getConfirmCopyrightInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CONFIRMCOPYRIGHT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
                );
    }

    public static CopyrightCert load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new CopyrightCert(contractAddress, client, credential);
    }

    public static CopyrightCert deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(CopyrightCert.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }
}
