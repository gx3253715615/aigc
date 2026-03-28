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
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class CopyrightCert extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610755806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c8063573d21f31461003b578063f6756f951461011c575b600080fd5b6100676004803603602081101561005157600080fd5b810190808035906020019092919050505061019f565b60405180806020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828103825285818151815260200191508051906020019080838360005b838110156100df5780820151818401526020810190506100c4565b50505050905090810190601f16801561010c5780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b61019d6004803603604081101561013257600080fd5b81019080803590602001909291908035906020019064010000000081111561015957600080fd5b82018360208201111561016b57600080fd5b8035906020019184600183028401116401000000008311171561018d57600080fd5b909192939192939050505061037c565b005b60606000806101ac61063a565b600080868152602001908152602001600020604051806080016040529081600082018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102625780601f1061023757610100808354040283529160200191610262565b820191906000526020600020905b81548152906001019060200180831161024557829003601f168201915b505050505081526020016001820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001600282015481526020016003820160009054906101000a900460ff1615151515815250509050806060015161035f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600f8152602001807f776f726b206e6f7420657869737473000000000000000000000000000000000081525060200191505060405180910390fd5b806000015181602001518260400151935093509350509193909250565b60008084815260200190815260200160002060030160009054906101000a900460ff1615610412576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f776f726b20616c7265616479206578697374730000000000000000000000000081525060200191505060405180910390fd5b6000828290501161048b576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600c8152602001807f696e76616c69642068617368000000000000000000000000000000000000000081525060200191505060405180910390fd5b6000429050604051806080016040528084848080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505081526020013373ffffffffffffffffffffffffffffffffffffffff16815260200182815260200160011515815250600080868152602001908152602001600020600082015181600001908051906020019061053c92919061067a565b5060208201518160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506040820151816002015560608201518160030160006101000a81548160ff0219169083151502179055509050503373ffffffffffffffffffffffffffffffffffffffff16847ff3c5c61902011292eb2bc23c25f1e3504d8b7f1c653176ea7dd28f7e8347ef9e85858560405180806020018381526020018281038252858582818152602001925080828437600081840152601f19601f82011690508083019250505094505050505060405180910390a350505050565b604051806080016040528060608152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600081526020016000151581525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106106bb57805160ff19168380011785556106e9565b828001600101855582156106e9579182015b828111156106e85782518255916020019190600101906106cd565b5b5090506106f691906106fa565b5090565b61071c91905b80821115610718576000816000905550600101610700565b5090565b9056fea26469706673582212203f1478bb82c373ec9427c0fa6512bea89b0267de8f657e58be61941ca2ca333f64736f6c634300060a0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50610755806100206000396000f3fe608060405234801561001057600080fd5b50600436106100365760003560e01c8063573d21f31461003b578063f6756f951461011c575b600080fd5b6100676004803603602081101561005157600080fd5b810190808035906020019092919050505061019f565b60405180806020018473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001838152602001828103825285818151815260200191508051906020019080838360005b838110156100df5780820151818401526020810190506100c4565b50505050905090810190601f16801561010c5780820380516001836020036101000a031916815260200191505b5094505050505060405180910390f35b61019d6004803603604081101561013257600080fd5b81019080803590602001909291908035906020019064010000000081111561015957600080fd5b82018360208201111561016b57600080fd5b8035906020019184600183028401116401000000008311171561018d57600080fd5b909192939192939050505061037c565b005b60606000806101ac61063a565b600080868152602001908152602001600020604051806080016040529081600082018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156102625780601f1061023757610100808354040283529160200191610262565b820191906000526020600020905b81548152906001019060200180831161024557829003601f168201915b505050505081526020016001820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001600282015481526020016003820160009054906101000a900460ff1615151515815250509050806060015161035f576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600f8152602001807f776f726b206e6f7420657869737473000000000000000000000000000000000081525060200191505060405180910390fd5b806000015181602001518260400151935093509350509193909250565b60008084815260200190815260200160002060030160009054906101000a900460ff1615610412576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260138152602001807f776f726b20616c7265616479206578697374730000000000000000000000000081525060200191505060405180910390fd5b6000828290501161048b576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040180806020018281038252600c8152602001807f696e76616c69642068617368000000000000000000000000000000000000000081525060200191505060405180910390fd5b6000429050604051806080016040528084848080601f016020809104026020016040519081016040528093929190818152602001838380828437600081840152601f19601f8201169050808301925050505050505081526020013373ffffffffffffffffffffffffffffffffffffffff16815260200182815260200160011515815250600080868152602001908152602001600020600082015181600001908051906020019061053c92919061067a565b5060208201518160010160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506040820151816002015560608201518160030160006101000a81548160ff0219169083151502179055509050503373ffffffffffffffffffffffffffffffffffffffff16847ff3c5c61902011292eb2bc23c25f1e3504d8b7f1c653176ea7dd28f7e8347ef9e85858560405180806020018381526020018281038252858582818152602001925080828437600081840152601f19601f82011690508083019250505094505050505060405180910390a350505050565b604051806080016040528060608152602001600073ffffffffffffffffffffffffffffffffffffffff168152602001600081526020016000151581525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106106bb57805160ff19168380011785556106e9565b828001600101855582156106e9579182015b828111156106e85782518255916020019190600101906106cd565b5b5090506106f691906106fa565b5090565b61071c91905b80821115610718576000816000905550600101610700565b5090565b9056fea26469706673582212203f1478bb82c373ec9427c0fa6512bea89b0267de8f657e58be61941ca2ca333f64736f6c634300060a0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"name\":\"CopyrightConfirmed\",\"type\":\"event\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":null,\"conflictFields\":[],\"inputs\":[{\"name\":\"workId\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":true,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"fileHash\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"author\",\"type\":\"address\",\"internalType\":\"address\",\"indexed\":true,\"components\":[],\"dynamic\":false,\"typeAsString\":\"address\"},{\"name\":\"certifyTime\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"}],\"outputs\":[],\"selector\":[],\"methodSignatureAsString\":\"CopyrightConfirmed(uint256,string,address,uint256)\"},{\"name\":\"confirmCopyright\",\"type\":\"function\",\"constant\":false,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"nonpayable\",\"conflictFields\":[],\"inputs\":[{\"name\":\"workId\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"},{\"name\":\"fileHash\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"}],\"outputs\":[],\"selector\":[],\"methodSignatureAsString\":\"confirmCopyright(uint256,string)\"},{\"name\":\"getWork\",\"type\":\"function\",\"constant\":true,\"payable\":false,\"anonymous\":false,\"stateMutability\":\"view\",\"conflictFields\":[],\"inputs\":[{\"name\":\"workId\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"}],\"outputs\":[{\"name\":\"fileHash\",\"type\":\"string\",\"internalType\":\"string\",\"indexed\":false,\"components\":[],\"dynamic\":true,\"typeAsString\":\"string\"},{\"name\":\"authorAddress\",\"type\":\"address\",\"internalType\":\"address\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"address\"},{\"name\":\"certifyTime\",\"type\":\"uint256\",\"internalType\":\"uint256\",\"indexed\":false,\"components\":[],\"dynamic\":false,\"typeAsString\":\"uint256\"}],\"selector\":[],\"methodSignatureAsString\":\"getWork(uint256)\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_CONFIRMCOPYRIGHT = "confirmCopyright";

    public static final String FUNC_GETWORK = "getWork";

    public static final Event COPYRIGHTCONFIRMED_EVENT = new Event("CopyrightConfirmed",
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
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
}
