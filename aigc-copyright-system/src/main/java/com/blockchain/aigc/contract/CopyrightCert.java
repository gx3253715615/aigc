package com.blockchain.aigc.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.codec.datatypes.Event;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple4;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class CopyrightCert extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610a27806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c8063573d21f3146100465780637192711f1461015a578063f6756f95146101a8575b600080fd5b6100726004803603602081101561005c57600080fd5b810190808035906020019092919050505061022b565b60405180806020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828103825286818151815260200191508051906020019080838360005b8381101561011c578082015181840152602081019050610101565b50505050905090810190601f1680156101495780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b6101a66004803603604081101561017057600080fd5b8101908080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610467565b005b610229600480360360408110156101be57600080fd5b8101908080359060200190929190803590602001906401000000008111156101e557600080fd5b8201836020820111156101f757600080fd5b8035906020019184600183028401116401000000008311171561021957600080fd5b90919293919293905050506105ce565b005b6060600080600061023a6108ef565b6000808781526020019081526020016000206040518060a0016040529081600082018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102f05780601f106102c5576101008083540402835291602001916102f0565b820191906000526020600020905b8154815290600101906020018083116102d357829003601f168201915b505050505081526020016001820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016002820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001600382015481526020016004820160009054906101000a900460ff16151515158152505090508060800151610443576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600f8152602001807f776f726b206e6f7420657869737473000000000000000000000000000000000081525060200191505060405180910390fd5b80600001518160200151826040015183606001519450945094509450509193509193565b600080600084815260200190815260200160002090508060040160009054906101000a900460ff16610501576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600a8152602001807f6e6f74206578697374730000000000000000000000000000000000000000000081525060200191505060405180910390fd5b60008160020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050828260020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508273ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16857fbb08ddfbfc8ff4660c18994aacdb0fdd2cadcc6137cacd5c933b1d9b7133b3dd60405160405180910390a450505050565b60008084815260200190815260200160002060040160009054906101000a900460ff1615610664576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f776f726b20616c7265616479206578697374730000000000000000000000000081525060200191505060405180910390fd5b600082829050116106dd576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600c8152602001807f696e76616c69642068617368000000000000000000000000000000000000000081525060200191505060405180910390fd5b60004290506040518060a0016040528084848080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505081526020013373ffffffffffffffffffffffffffffffffffffffff1681526020013373ffffffffffffffffffffffffffffffffffffffff1681526020018281526020016001151581525060008086815260200190815260200160002060008201518160000190805190602001906107aa92919061094c565b5060208201518160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060408201518160020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506060820151816003015560808201518160040160006101000a81548160ff0219169083151502179055509050503373ffffffffffffffffffffffffffffffffffffffff16847ff3c5c61902011292eb2bc23c25f1e3504d8b7f1c653176ea7dd28f7e8347ef9e85858560405180806020018381526020018281038252858582818152602001925080828437600081840152601f19601f82011690508083019250505094505050505060405180910390a350505050565b6040518060a0016040528060608152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600081526020016000151581525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061098d57805160ff19168380011785556109bb565b828001600101855582156109bb579182015b828111156109ba57825182559160200191906001019061099f565b5b5090506109c891906109cc565b5090565b6109ee91905b808211156109ea5760008160009055506001016109d2565b5090565b9056fea2646970667358221220d60f6e350a4b674a80fc6e199c8510fe590fbd66faeed6bd621d26070d6ebc0164736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50610a27806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c8063573d21f3146100465780637192711f1461015a578063f6756f95146101a8575b600080fd5b6100726004803603602081101561005c57600080fd5b810190808035906020019092919050505061022b565b60405180806020018573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828103825286818151815260200191508051906020019080838360005b8381101561011c578082015181840152602081019050610101565b50505050905090810190601f1680156101495780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b6101a66004803603604081101561017057600080fd5b8101908080359060200190929190803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610467565b005b610229600480360360408110156101be57600080fd5b8101908080359060200190929190803590602001906401000000008111156101e557600080fd5b8201836020820111156101f757600080fd5b8035906020019184600183028401116401000000008311171561021957600080fd5b90919293919293905050506105ce565b005b6060600080600061023a6108ef565b6000808781526020019081526020016000206040518060a0016040529081600082018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102f05780601f106102c5576101008083540402835291602001916102f0565b820191906000526020600020905b8154815290600101906020018083116102d357829003601f168201915b505050505081526020016001820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016002820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001600382015481526020016004820160009054906101000a900460ff16151515158152505090508060800151610443576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600f8152602001807f776f726b206e6f7420657869737473000000000000000000000000000000000081525060200191505060405180910390fd5b80600001518160200151826040015183606001519450945094509450509193509193565b600080600084815260200190815260200160002090508060040160009054906101000a900460ff16610501576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600a8152602001807f6e6f74206578697374730000000000000000000000000000000000000000000081525060200191505060405180910390fd5b60008160020160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff169050828260020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055508273ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16857fbb08ddfbfc8ff4660c18994aacdb0fdd2cadcc6137cacd5c933b1d9b7133b3dd60405160405180910390a450505050565b60008084815260200190815260200160002060040160009054906101000a900460ff1615610664576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f776f726b20616c7265616479206578697374730000000000000000000000000081525060200191505060405180910390fd5b600082829050116106dd576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600c8152602001807f696e76616c69642068617368000000000000000000000000000000000000000081525060200191505060405180910390fd5b60004290506040518060a0016040528084848080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505081526020013373ffffffffffffffffffffffffffffffffffffffff1681526020013373ffffffffffffffffffffffffffffffffffffffff1681526020018281526020016001151581525060008086815260200190815260200160002060008201518160000190805190602001906107aa92919061094c565b5060208201518160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060408201518160020160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506060820151816003015560808201518160040160006101000a81548160ff0219169083151502179055509050503373ffffffffffffffffffffffffffffffffffffffff16847ff3c5c61902011292eb2bc23c25f1e3504d8b7f1c653176ea7dd28f7e8347ef9e85858560405180806020018381526020018281038252858582818152602001925080828437600081840152601f19601f82011690508083019250505094505050505060405180910390a350505050565b6040518060a0016040528060608152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600081526020016000151581525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061098d57805160ff19168380011785556109bb565b828001600101855582156109bb579182015b828111156109ba57825182559160200191906001019061099f565b5b5090506109c891906109cc565b5090565b6109ee91905b808211156109ea5760008160009055506001016109d2565b5090565b9056fea2646970667358221220d60f6e350a4b674a80fc6e199c8510fe590fbd66faeed6bd621d26070d6ebc0164736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"name\":\"CopyrightConfirmed\",\"type\":\"event\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":null,\"conflictFields\":[],\"inputs\":[{\"name\":\"workId\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":true,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"fileHash\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"author\",\"type\":\"address\",\"internalType\":\"address\",\"indexed\":true,\"components\":[],\"dynamic\":false,\"typeAsString\":\"address\"},{\"name\":\"certifyTime\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"}],\"outputs\":[],\"selector\":[],\"methodSignatureAsString\":\"CopyrightConfirmed(uint256,string,address,uint256)\"},{\"name\":\"OwnerUpdated\",\"type\":\"event\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":null,\"conflictFields\":[],\"inputs\":[{\"name\":\"workId\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":true,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"oldOwner\",\"type\":\"address\",\"internalType\":\"address\",\"indexed\":true,\"components\":[],\"dynamic\":false,\"typeAsString\":\"address\"},{\"name\":\"newOwner\",\"type\":\"address\",\"internalType\":\"address\",\"indexed\":true,\"components\":[],\"dynamic\":false,\"typeAsString\":\"address\"}],\"outputs\":[],\"selector\":[],\"methodSignatureAsString\":\"OwnerUpdated(uint256,address,address)\"},{\"name\":\"confirmCopyright\",\"type\":\"function\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"conflictFields\":[],\"inputs\":[{\"name\":\"workId\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"fileHash\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[],\"selector\":[],\"methodSignatureAsString\":\"confirmCopyright(uint256,string)\"},{\"name\":\"getWork\",\"type\":\"function\",\"constant\":true,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"view\",\"conflictFields\":[],\"inputs\":[{\"name\":\"workId\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"}],\"outputs\":[{\"name\":\"fileHash\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"authorAddress\",\"type\":\"address\",\"internalType\":\"address\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"address\"},{\"name\":\"owner\",\"type\":\"address\",\"internalType\":\"address\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"address\"},{\"name\":\"certifyTime\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"}],\"selector\":[],\"methodSignatureAsString\":\"getWork(uint256)\"},{\"name\":\"updateOwner\",\"type\":\"function\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"conflictFields\":[],\"inputs\":[{\"name\":\"workId\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"newOwner\",\"type\":\"address\",\"internalType\":\"address\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"address\"}],\"outputs\":[],\"selector\":[],\"methodSignatureAsString\":\"updateOwner(uint256,address)\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_CONFIRMCOPYRIGHT = "confirmCopyright";

    public static final String FUNC_GETWORK = "getWork";

    public static final String FUNC_UPDATEOWNER = "updateOwner";

    public static final Event COPYRIGHTCONFIRMED_EVENT = new Event("CopyrightConfirmed",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERUPDATED_EVENT = new Event("OwnerUpdated",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    protected CopyrightCert(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public List<CopyrightConfirmedEventResponse> getCopyrightConfirmedEvents(
            TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(COPYRIGHTCONFIRMED_EVENT, transactionReceipt);
        ArrayList<CopyrightConfirmedEventResponse> responses = new ArrayList<CopyrightConfirmedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            CopyrightConfirmedEventResponse typedResponse = new CopyrightConfirmedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.workId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.author = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.fileHash = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.certifyTime = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public List<OwnerUpdatedEventResponse> getOwnerUpdatedEvents(
            TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERUPDATED_EVENT, transactionReceipt);
        ArrayList<OwnerUpdatedEventResponse> responses = new ArrayList<OwnerUpdatedEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OwnerUpdatedEventResponse typedResponse = new OwnerUpdatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.workId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.oldOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public TransactionReceipt confirmCopyright(BigInteger workId, String fileHash) {
        final Function function = new Function(
                FUNC_CONFIRMCOPYRIGHT,
                Arrays.<Type>asList(new Uint256(workId),
                new Utf8String(fileHash)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String getSignedTransactionForConfirmCopyright(BigInteger workId, String fileHash) {
        final Function function = new Function(
                FUNC_CONFIRMCOPYRIGHT,
                Arrays.<Type>asList(new Uint256(workId),
                new Utf8String(fileHash)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public String confirmCopyright(BigInteger workId, String fileHash,
            TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CONFIRMCOPYRIGHT,
                Arrays.<Type>asList(new Uint256(workId),
                new Utf8String(fileHash)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple2<BigInteger, String> getConfirmCopyrightInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CONFIRMCOPYRIGHT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<BigInteger, String>(

                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue()
                );
    }

    public Tuple4<String, String, String, BigInteger> getWork(BigInteger workId) throws
            ContractException {
        final Function function = new Function(FUNC_GETWORK,
                Arrays.<Type>asList(new Uint256(workId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple4<String, String, String, BigInteger>(
                (String) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (BigInteger) results.get(3).getValue());
    }

    public TransactionReceipt updateOwner(BigInteger workId, String newOwner) {
        final Function function = new Function(
                FUNC_UPDATEOWNER,
                Arrays.<Type>asList(new Uint256(workId),
                new Address(newOwner)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public String getSignedTransactionForUpdateOwner(BigInteger workId, String newOwner) {
        final Function function = new Function(
                FUNC_UPDATEOWNER,
                Arrays.<Type>asList(new Uint256(workId),
                new Address(newOwner)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public String updateOwner(BigInteger workId, String newOwner, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_UPDATEOWNER,
                Arrays.<Type>asList(new Uint256(workId),
                new Address(newOwner)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple2<BigInteger, String> getUpdateOwnerInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_UPDATEOWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple2<BigInteger, String>(

                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue()
                );
    }

    public static CopyrightCert load(String contractAddress, Client client,
            CryptoKeyPair credential) {
        return new CopyrightCert(contractAddress, client, credential);
    }

    public static CopyrightCert deploy(Client client, CryptoKeyPair credential) throws
            ContractException {
        return deploy(CopyrightCert.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), null, null);
    }

    public static class CopyrightConfirmedEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger workId;

        public String author;

        public String fileHash;

        public BigInteger certifyTime;
    }

    public static class OwnerUpdatedEventResponse {
        public TransactionReceipt.Logs log;

        public BigInteger workId;

        public String oldOwner;

        public String newOwner;
    }
}
