package com.blockchain.aigc.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.abi.FunctionEncoder;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.codec.datatypes.DynamicArray;
import org.fisco.bcos.sdk.v3.codec.datatypes.Event;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple10;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple9;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class CopyrightTransfer extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b5060405161117a38038061117a8339818101604052602081101561003357600080fd5b810190808051906020019092919050505080600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506110e5806100956000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c806358c3805a1461005157806362df91111461009b578063c16fe9071461011e578063e20e7e7b14610278575b600080fd5b6100596103a0565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6100c7600480360360208110156100b157600080fd5b81019080803590602001909291905050506103c6565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b8381101561010a5780820151818401526020810190506100ef565b505050509050019250505060405180910390f35b61014a6004803603602081101561013457600080fd5b8101908080359060200190929190505050610431565b604051808b8152602001806020018a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018860028111156101c857fe5b60ff1681526020018760038111156101dc57fe5b60ff16815260200186815260200185815260200184815260200183815260200182810382528b818151815260200191508051906020019080838360005b83811015610234578082015181840152602081019050610219565b50505050905090810190601f1680156102615780820380516001836020036101000a031916815260200191505b509b50505050505050505050505060405180910390f35b61039e600480360361012081101561028f57600080fd5b810190808035906020019092919080359060200190929190803590602001906401000000008111156102c057600080fd5b8201836020820111156102d257600080fd5b803590602001918460018302840111640100000000831117156102f457600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803560ff169060200190929190803560ff16906020019092919080359060200190929190803590602001909291908035906020019092919050505061061b565b005b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60606001600083815260200190815260200160002080548060200260200160405190810160405280929190818152602001828054801561042557602002820191906000526020600020905b815481526020019060010190808311610411575b50505050509050919050565b6000606060008060008060008060008060008060008d815260200190815260200160002090506000816008015414156104d2576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f7472616e73666572206e6f74206578697374730000000000000000000000000081525060200191505060405180910390fd5b8060010154816002018260030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168360040160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168460040160149054906101000a900460ff168560040160159054906101000a900460ff168660050154876006015488600701548960080154888054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105f25780601f106105c7576101008083540402835291602001916105f2565b820191906000526020600020905b8154815290600101906020018083116105d557829003601f168201915b505050505098509a509a509a509a509a509a509a509a509a509a50509193959799509193959799565b60008060008b815260200190815260200160002060080154146106a6576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600f8152602001807f7472616e7366657220657869737473000000000000000000000000000000000081525060200191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168673ffffffffffffffffffffffffffffffffffffffff161415610749576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f696e76616c696420746f2061646472657373000000000000000000000000000081525060200191505060405180910390fd5b60008110156107c0576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600e8152602001807f696e76616c696420616d6f756e7400000000000000000000000000000000000081525060200191505060405180910390fd5b600060028111156107cd57fe5b8560028111156107d957fe5b141561086c57600060038111156107ec57fe5b8460038111156107f857fe5b1461086b576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f66756c6c207472616e73666572206e6f206c6963656e7365000000000000000081525060200191505060405180910390fd5b5b6001600281111561087957fe5b85600281111561088557fe5b141561099a576000600381111561089857fe5b8460038111156108a457fe5b1415610918576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260108152602001807f6c6963656e73652072657175697265640000000000000000000000000000000081525060200191505060405180910390fd5b60008311801561092757508282115b610999576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600c8152602001807f696e76616c69642074696d65000000000000000000000000000000000000000081525060200191505060405180910390fd5b5b6000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663573d21f38a6040518263ffffffff1660e01b81526004018082815260200191505060006040518083038186803b158015610a0f57600080fd5b505afa158015610a23573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f820116820180604052506080811015610a4d57600080fd5b8101908080516040519392919084640100000000821115610a6d57600080fd5b83820191506020820185811115610a8357600080fd5b8251866001820283011164010000000082111715610aa057600080fd5b8083526020830192505050908051906020019080838360005b83811015610ad4578082015181840152602081019050610ab9565b50505050905090810190601f168015610b015780820380516001836020036101000a031916815260200191505b5060405260200180519060200190929190805190602001909291908051906020019092919050505050925050508073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610bcf576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260178152602001807f6f6e6c79206f776e65722063616e207472616e7366657200000000000000000081525060200191505060405180910390fd5b6040518061016001604052808b81526020018a81526020018981526020018273ffffffffffffffffffffffffffffffffffffffff1681526020018873ffffffffffffffffffffffffffffffffffffffff168152602001876002811115610c3157fe5b8152602001866003811115610c4257fe5b8152602001858152602001848152602001838152602001428152506000808c815260200190815260200160002060008201518160000155602082015181600101556040820151816002019080519060200190610c9f92919061100a565b5060608201518160030160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060808201518160040160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060a08201518160040160146101000a81548160ff02191690836002811115610d5257fe5b021790555060c08201518160040160156101000a81548160ff02191690836003811115610d7b57fe5b021790555060e08201518160050155610100820151816006015561012082015181600701556101408201518160080155905050600160008a81526020019081526020016000208a908060018154018082558091505060019003906000526020600020016000909190919091505560006002811115610df557fe5b866002811115610e0157fe5b1415610ec957600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16637192711f8a896040518363ffffffff1660e01b8152600401808381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200192505050600060405180830381600087803b158015610eb057600080fd5b505af1158015610ec4573d6000803e3d6000fd5b505050505b8073ffffffffffffffffffffffffffffffffffffffff16898b7f9a2fda50d5e93ba6c4371b04418354c5fc6cc9e8d285906e6770dce5cd7958428a8c8b8b8b8b8b42604051808973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001886002811115610f5157fe5b60ff168152602001876003811115610f6557fe5b60ff168152","602001868152602001858152602001848152602001838152602001828103825289818151815260200191508051906020019080838360005b83811015610fbd578082015181840152602081019050610fa2565b50505050905090810190601f168015610fea5780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390a450505050505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061104b57805160ff1916838001178555611079565b82800160010185558215611079579182015b8281111561107857825182559160200191906001019061105d565b5b509050611086919061108a565b5090565b6110ac91905b808211156110a8576000816000905550600101611090565b5090565b9056fea264697066735822122099d8d38172c72289b7a23f21051937c24eb3389cba52d399907137043c33039764736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b5060405161117a38038061117a8339818101604052602081101561003357600080fd5b810190808051906020019092919050505080600260006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550506110e5806100956000396000f3fe608060405234801561001057600080fd5b506004361061004c5760003560e01c806358c3805a1461005157806362df91111461009b578063c16fe9071461011e578063e20e7e7b14610278575b600080fd5b6100596103a0565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b6100c7600480360360208110156100b157600080fd5b81019080803590602001909291905050506103c6565b6040518080602001828103825283818151815260200191508051906020019060200280838360005b8381101561010a5780820151818401526020810190506100ef565b505050509050019250505060405180910390f35b61014a6004803603602081101561013457600080fd5b8101908080359060200190929190505050610431565b604051808b8152602001806020018a73ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018860028111156101c857fe5b60ff1681526020018760038111156101dc57fe5b60ff16815260200186815260200185815260200184815260200183815260200182810382528b818151815260200191508051906020019080838360005b83811015610234578082015181840152602081019050610219565b50505050905090810190601f1680156102615780820380516001836020036101000a031916815260200191505b509b50505050505050505050505060405180910390f35b61039e600480360361012081101561028f57600080fd5b810190808035906020019092919080359060200190929190803590602001906401000000008111156102c057600080fd5b8201836020820111156102d257600080fd5b803590602001918460018302840111640100000000831117156102f457600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f820116905080830192505050505050509192919290803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803560ff169060200190929190803560ff16906020019092919080359060200190929190803590602001909291908035906020019092919050505061061b565b005b600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60606001600083815260200190815260200160002080548060200260200160405190810160405280929190818152602001828054801561042557602002820191906000526020600020905b815481526020019060010190808311610411575b50505050509050919050565b6000606060008060008060008060008060008060008d815260200190815260200160002090506000816008015414156104d2576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f7472616e73666572206e6f74206578697374730000000000000000000000000081525060200191505060405180910390fd5b8060010154816002018260030160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168360040160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff168460040160149054906101000a900460ff168560040160159054906101000a900460ff168660050154876006015488600701548960080154888054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105f25780601f106105c7576101008083540402835291602001916105f2565b820191906000526020600020905b8154815290600101906020018083116105d557829003601f168201915b505050505098509a509a509a509a509a509a509a509a509a509a50509193959799509193959799565b60008060008b815260200190815260200160002060080154146106a6576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600f8152602001807f7472616e7366657220657869737473000000000000000000000000000000000081525060200191505060405180910390fd5b600073ffffffffffffffffffffffffffffffffffffffff168673ffffffffffffffffffffffffffffffffffffffff161415610749576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260128152602001807f696e76616c696420746f2061646472657373000000000000000000000000000081525060200191505060405180910390fd5b60008110156107c0576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600e8152602001807f696e76616c696420616d6f756e7400000000000000000000000000000000000081525060200191505060405180910390fd5b600060028111156107cd57fe5b8560028111156107d957fe5b141561086c57600060038111156107ec57fe5b8460038111156107f857fe5b1461086b576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260188152602001807f66756c6c207472616e73666572206e6f206c6963656e7365000000000000000081525060200191505060405180910390fd5b5b6001600281111561087957fe5b85600281111561088557fe5b141561099a576000600381111561089857fe5b8460038111156108a457fe5b1415610918576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260108152602001807f6c6963656e73652072657175697265640000000000000000000000000000000081525060200191505060405180910390fd5b60008311801561092757508282115b610999576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600c8152602001807f696e76616c69642074696d65000000000000000000000000000000000000000081525060200191505060405180910390fd5b5b6000600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663573d21f38a6040518263ffffffff1660e01b81526004018082815260200191505060006040518083038186803b158015610a0f57600080fd5b505afa158015610a23573d6000803e3d6000fd5b505050506040513d6000823e3d601f19601f820116820180604052506080811015610a4d57600080fd5b8101908080516040519392919084640100000000821115610a6d57600080fd5b83820191506020820185811115610a8357600080fd5b8251866001820283011164010000000082111715610aa057600080fd5b8083526020830192505050908051906020019080838360005b83811015610ad4578082015181840152602081019050610ab9565b50505050905090810190601f168015610b015780820380516001836020036101000a031916815260200191505b5060405260200180519060200190929190805190602001909291908051906020019092919050505050925050508073ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614610bcf576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260178152602001807f6f6e6c79206f776e65722063616e207472616e7366657200000000000000000081525060200191505060405180910390fd5b6040518061016001604052808b81526020018a81526020018981526020018273ffffffffffffffffffffffffffffffffffffffff1681526020018873ffffffffffffffffffffffffffffffffffffffff168152602001876002811115610c3157fe5b8152602001866003811115610c4257fe5b8152602001858152602001848152602001838152602001428152506000808c815260200190815260200160002060008201518160000155602082015181600101556040820151816002019080519060200190610c9f92919061100a565b5060608201518160030160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060808201518160040160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060a08201518160040160146101000a81548160ff02191690836002811115610d5257fe5b021790555060c08201518160040160156101000a81548160ff02191690836003811115610d7b57fe5b021790555060e08201518160050155610100820151816006015561012082015181600701556101408201518160080155905050600160008a81526020019081526020016000208a908060018154018082558091505060019003906000526020600020016000909190919091505560006002811115610df557fe5b866002811115610e0157fe5b1415610ec957600260009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16637192711f8a896040518363ffffffff1660e01b8152600401808381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200192505050600060405180830381600087803b158015610eb057600080fd5b505af1158015610ec4573d6000803e3d6000fd5b505050505b8073ffffffffffffffffffffffffffffffffffffffff16898b7f9a2fda50d5e93ba6c4371b04418354c5fc6cc9e8d285906e6770dce5cd7958428a8c8b8b8b8b8b42604051808973ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001886002811115610f5157fe5b60ff168152602001876003811115610f6557fe5b60ff168152","602001868152602001858152602001848152602001838152602001828103825289818151815260200191508051906020019080838360005b83811015610fbd578082015181840152602081019050610fa2565b50505050905090810190601f168015610fea5780820380516001836020036101000a031916815260200191505b50995050505050505050505060405180910390a450505050505050505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061104b57805160ff1916838001178555611079565b82800160010185558215611079579182015b8281111561107857825182559160200191906001019061105d565b5b509050611086919061108a565b5090565b6110ac91905b808211156110a8576000816000905550600101611090565b5090565b9056fea264697066735822122099d8d38172c72289b7a23f21051937c24eb3389cba52d399907137043c33039764736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"name\":null,\"type\":\"constructor\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"conflictFields\":[],\"inputs\":[{\"name\":\"_workContract\",\"type\":\"address\",\"internalType\":\"address\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"address\"}],\"outputs\":[],\"selector\":[],\"methodSignatureAsString\":\"null(address)\"},{\"name\":\"TransferCreated\",\"type\":\"event\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":null,\"conflictFields\":[],\"inputs\":[{\"name\":\"transferId\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":true,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"workId\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":true,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"from\",\"type\":\"address\",\"internalType\":\"address\",\"indexed\":true,\"components\":[],\"dynamic\":false,\"typeAsString\":\"address\"},{\"name\":\"to\",\"type\":\"address\",\"internalType\":\"address\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"address\"},{\"name\":\"fileHash\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"transferType\",\"type\":\"uint8\",\"internalType\":\"enum CopyrightTransfer.TransferType\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint8\"},{\"name\":\"licenseType\",\"type\":\"uint8\",\"internalType\":\"enum CopyrightTransfer.LicenseType\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint8\"},{\"name\":\"effectiveTime\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"expireTime\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"amount\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"timestamp\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"}],\"outputs\":[],\"selector\":[],\"methodSignatureAsString\":\"TransferCreated(uint256,uint256,address,address,string,uint8,uint8,uint256,uint256,uint256,uint256)\"},{\"name\":\"createTransfer\",\"type\":\"function\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"conflictFields\":[],\"inputs\":[{\"name\":\"transferId\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"workId\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"fileHash\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"to\",\"type\":\"address\",\"internalType\":\"address\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"address\"},{\"name\":\"transferType\",\"type\":\"uint8\",\"internalType\":\"enum CopyrightTransfer.TransferType\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint8\"},{\"name\":\"licenseType\",\"type\":\"uint8\",\"internalType\":\"enum CopyrightTransfer.LicenseType\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint8\"},{\"name\":\"effectiveTime\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"expireTime\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"amount\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"}],\"outputs\":[],\"selector\":[],\"methodSignatureAsString\":\"createTransfer(uint256,uint256,string,address,uint8,uint8,uint256,uint256,uint256)\"},{\"name\":\"getTransfer\",\"type\":\"function\",\"constant\":true,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"view\",\"conflictFields\":[],\"inputs\":[{\"name\":\"transferId\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"}],\"outputs\":[{\"name\":\"workId\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"fileHash\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"from\",\"type\":\"address\",\"internalType\":\"address\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"address\"},{\"name\":\"to\",\"type\":\"address\",\"internalType\":\"address\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"address\"},{\"name\":\"transferType\",\"type\":\"uint8\",\"internalType\":\"enum CopyrightTransfer.TransferType\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint8\"},{\"name\":\"licenseType\",\"type\":\"uint8\",\"internalType\":\"enum CopyrightTransfer.LicenseType\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint8\"},{\"name\":\"effectiveTime\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"expireTime\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"amount\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"timestamp\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"}],\"selector\":[],\"methodSignatureAsString\":\"getTransfer(uint256)\"},{\"name\":\"getWorkTransfers\",\"type\":\"function\",\"constant\":true,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"view\",\"conflictFields\":[],\"inputs\":[{\"name\":\"workId\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"}],\"outputs\":[{\"name\":\"\",\"type\":\"uint256[]\",\"internalType\":\"uint256[]\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"uint256[]\"}],\"selector\":[],\"methodSignatureAsString\":\"getWorkTransfers(uint256)\"},{\"name\":\"workContract\",\"type\":\"function\",\"constant\":true,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"view\",\"conflictFields\":[],\"inputs\":[],\"outputs\":[{\"name\":\"\",\"type\":\"address\",\"internalType\":\"address\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"address\"}],\"selector\":[],\"methodSignatureAsString\":\"workContract()\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_CREATETRANSFER = "createTransfer";

    public static final String FUNC_GETTRANSFER = "getTransfer";

    public static final String FUNC_GETWORKTRANSFERS = "getWorkTransfers";

    public static final String FUNC_WORKCONTRACT = "workContract";

    public static final Event TRANSFERCREATED_EVENT = new Event("TransferCreated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    protected CopyrightTransfer(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public List<TransferCreatedEventResponse> getTransferCreatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFERCREATED_EVENT, transactionReceipt);
        ArrayList<TransferCreatedEventResponse> responses = new ArrayList<TransferCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferCreatedEventResponse typedResponse = new TransferCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.transferId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.workId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.from = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse.to = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.fileHash = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.transferType = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.licenseType = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.effectiveTime = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.expireTime = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(7).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public TransactionReceipt createTransfer(BigInteger transferId, BigInteger workId,
            String fileHash, String to, BigInteger transferType, BigInteger licenseType,
            BigInteger effectiveTime, BigInteger expireTime, BigInteger amount) {
        final Function function = new Function(
                FUNC_CREATETRANSFER,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(transferId),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(workId),
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(fileHash),
                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(to),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(transferType),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(licenseType),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(effectiveTime),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(expireTime),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String getSignedTransactionForCreateTransfer(BigInteger transferId, BigInteger workId,
            String fileHash, String to, BigInteger transferType, BigInteger licenseType,
            BigInteger effectiveTime, BigInteger expireTime, BigInteger amount) {
        final Function function = new Function(
                FUNC_CREATETRANSFER,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(transferId),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(workId),
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(fileHash),
                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(to),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(transferType),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(licenseType),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(effectiveTime),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(expireTime),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public String createTransfer(BigInteger transferId, BigInteger workId, String fileHash,
            String to, BigInteger transferType, BigInteger licenseType, BigInteger effectiveTime,
            BigInteger expireTime, BigInteger amount, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CREATETRANSFER,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(transferId),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(workId),
                new org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String(fileHash),
                new org.fisco.bcos.sdk.v3.codec.datatypes.Address(to),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(transferType),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8(licenseType),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(effectiveTime),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(expireTime),
                new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(amount)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple9<BigInteger, BigInteger, String, String, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger> getCreateTransferInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CREATETRANSFER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple9<BigInteger, BigInteger, String, String, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>(

                (BigInteger) results.get(0).getValue(),
                (BigInteger) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (BigInteger) results.get(4).getValue(),
                (BigInteger) results.get(5).getValue(),
                (BigInteger) results.get(6).getValue(),
                (BigInteger) results.get(7).getValue(),
                (BigInteger) results.get(8).getValue()
                );
    }

    public Tuple10<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger> getTransfer(
            BigInteger transferId) throws ContractException {
        final Function function = new Function(FUNC_GETTRANSFER,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(transferId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple10<BigInteger, String, String, String, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>(
                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (BigInteger) results.get(4).getValue(),
                (BigInteger) results.get(5).getValue(),
                (BigInteger) results.get(6).getValue(),
                (BigInteger) results.get(7).getValue(),
                (BigInteger) results.get(8).getValue(),
                (BigInteger) results.get(9).getValue());
    }

    public List getWorkTransfers(BigInteger workId) throws ContractException {
        final Function function = new Function(FUNC_GETWORKTRANSFERS,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256(workId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        List<Type> result = (List<Type>) executeCallWithSingleValueReturn(function, List.class);
        return convertToNative(result);
    }

    public String workContract() throws ContractException {
        final Function function = new Function(FUNC_WORKCONTRACT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public static CopyrightTransfer load(String contractAddress, Client client,
            CryptoKeyPair credential) {
        return new CopyrightTransfer(contractAddress, client, credential);
    }

    public static CopyrightTransfer deploy(Client client, CryptoKeyPair credential,
            String _workContract) throws ContractException {
        byte[] encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.fisco.bcos.sdk.v3.codec.datatypes.Address(_workContract)));
        return deploy(CopyrightTransfer.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), encodedConstructor, null);
    }

    public static class TransferCreatedEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger transferId;

        public BigInteger workId;

        public String from;

        public String to;

        public String fileHash;

        public BigInteger transferType;

        public BigInteger licenseType;

        public BigInteger effectiveTime;

        public BigInteger expireTime;

        public BigInteger amount;

        public BigInteger timestamp;
    }
}
