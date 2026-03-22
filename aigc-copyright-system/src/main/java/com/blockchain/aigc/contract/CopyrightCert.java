package com.blockchain.aigc.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple1;
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
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b5061062d806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c8063573d21f3146100465780636052d31f146100715780636b18828814610086575b600080fd5b610059610054366004610439565b610099565b60405161006893929190610452565b60405180910390f35b61008461007f3660046104f1565b6101aa565b005b610084610094366004610439565b6102a0565b6000818152602081905260408120600381015460609291829160ff166100f85760405162461bcd60e51b815260206004820152600f60248201526e776f726b206e6f742065786973747360881b60448201526064015b60405180910390fd5b60018101546002820154825483926001600160a01b03169190839061011c906105bc565b80601f0160208091040260200160405190810160405280929190818152602001828054610148906105bc565b80156101955780601f1061016a57610100808354040283529160200191610195565b820191906000526020600020905b81548152906001019060200180831161017857829003601f168201915b50505050509250935093509350509193909250565b60008381526020819052604090206003015460ff16156102025760405162461bcd60e51b8152602060048201526013602482015272776f726b20616c72656164792065786973747360681b60448201526064016100ef565b604080516080810182528381526001600160a01b03831660208083019190915260008284018190526001606084015286815280825292909220815180519293919261025092849201906103a0565b5060208201516001820180546001600160a01b0319166001600160a01b03909216919091179055604082015160028201556060909101516003909101805460ff1916911515919091179055505050565b6000818152602081905260409020600381015460ff166102f45760405162461bcd60e51b815260206004820152600f60248201526e776f726b206e6f742065786973747360881b60448201526064016100ef565b60018101546001600160a01b031633146103505760405162461bcd60e51b815260206004820152601760248201527f6f6e6c7920617574686f722063616e20636f6e6669726d00000000000000000060448201526064016100ef565b6002810154156103965760405162461bcd60e51b8152602060048201526011602482015270185b1c9958591e4818dbdb999a5c9b5959607a1b60448201526064016100ef565b4260029091015550565b8280546103ac906105bc565b90600052602060002090601f0160209004810192826103ce5760008555610414565b82601f106103e757805160ff1916838001178555610414565b82800160010185558215610414579182015b828111156104145782518255916020019190600101906103f9565b50610420929150610424565b5090565b5b808211156104205760008155600101610425565b60006020828403121561044b57600080fd5b5035919050565b606081526000845180606084015260005b818110156104805760208188018101516080868401015201610463565b81811115610492576000608083860101525b506001600160a01b039490941660208301525060408101919091526080601f909201601f19160101919050565b634e487b7160e01b600052604160045260246000fd5b80356001600160a01b03811681146104ec57600080fd5b919050565b60008060006060848603121561050657600080fd5b83359250602084013567ffffffffffffffff8082111561052557600080fd5b818601915086601f83011261053957600080fd5b81358181111561054b5761054b6104bf565b604051601f8201601f19908116603f01168101908382118183101715610573576105736104bf565b8160405282815289602084870101111561058c57600080fd5b8260208601602083013760006020848301015280965050505050506105b3604085016104d5565b90509250925092565b600181811c908216806105d057607f821691505b602082108114156105f157634e487b7160e01b600052602260045260246000fd5b5091905056fea264697066735822122029df4482102a61a2004cb011960f3426e33093d77cf0c9dad4cdb1cea7fbc21564736f6c634300080b0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50610632806100206000396000f3fe608060405234801561001057600080fd5b50600436106100415760003560e01c80631f3f4dd214610046578063b0517bf41461005b578063e25601e11461006e575b600080fd5b61005961005436600461043e565b610099565b005b610059610069366004610489565b6101a1565b61008161007c36600461043e565b610298565b60405161009093929190610554565b60405180910390f35b6000818152602081905260409020600381015460ff166100f357604051636381e58960e11b815260206004820152600f60248201526e776f726b206e6f742065786973747360881b60448201526064015b60405180910390fd5b60018101546001600160a01b0316331461015057604051636381e58960e11b815260206004820152601760248201527f6f6e6c7920617574686f722063616e20636f6e6669726d00000000000000000060448201526064016100ea565b60028101541561019757604051636381e58960e11b8152602060048201526011602482015270185b1c9958591e4818dbdb999a5c9b5959607a1b60448201526064016100ea565b4260029091015550565b60008381526020819052604090206003015460ff16156101fa57604051636381e58960e11b8152602060048201526013602482015272776f726b20616c72656164792065786973747360681b60448201526064016100ea565b604080516080810182528381526001600160a01b03831660208083019190915260008284018190526001606084015286815280825292909220815180519293919261024892849201906103a5565b5060208201516001820180546001600160a01b0319166001600160a01b03909216919091179055604082015160028201556060909101516003909101805460ff1916911515919091179055505050565b6000818152602081905260408120600381015460609291829160ff166102f357604051636381e58960e11b815260206004820152600f60248201526e776f726b206e6f742065786973747360881b60448201526064016100ea565b60018101546002820154825483926001600160a01b031691908390610317906105c1565b80601f0160208091040260200160405190810160405280929190818152602001828054610343906105c1565b80156103905780601f1061036557610100808354040283529160200191610390565b820191906000526020600020905b81548152906001019060200180831161037357829003601f168201915b50505050509250935093509350509193909250565b8280546103b1906105c1565b90600052602060002090601f0160209004810192826103d35760008555610419565b82601f106103ec57805160ff1916838001178555610419565b82800160010185558215610419579182015b828111156104195782518255916020019190600101906103fe565b50610425929150610429565b5090565b5b80821115610425576000815560010161042a565b60006020828403121561045057600080fd5b5035919050565b63b95aa35560e01b600052604160045260246000fd5b80356001600160a01b038116811461048457600080fd5b919050565b60008060006060848603121561049e57600080fd5b83359250602084013567ffffffffffffffff808211156104bd57600080fd5b818601915086601f8301126104d157600080fd5b8135818111156104e3576104e3610457565b604051601f8201601f19908116603f0116810190838211818310171561050b5761050b610457565b8160405282815289602084870101111561052457600080fd5b82602086016020830137600060208483010152809650505050505061054b6040850161046d565b90509250925092565b606081526000845180606084015260005b818110156105825760208188018101516080868401015201610565565b81811115610594576000608083860101525b506001600160a01b039490941660208301525060408101919091526080601f909201601f19160101919050565b600181811c908216806105d557607f821691505b602082108114156105f65763b95aa35560e01b600052602260045260246000fd5b5091905056fea2646970667358221220f5ee5940a247e6b823e19ea6b3879e5e8a18b1437487c79392d53c575378d90b64736f6c634300080b0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"workId\",\"type\":\"uint256\"}],\"name\":\"confirmCopyright\",\"outputs\":[],\"selector\":[1796768392,524242386],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"workId\",\"type\":\"uint256\"}],\"name\":\"getWork\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"fileHash\",\"type\":\"string\"},{\"internalType\":\"address\",\"name\":\"authorAddress\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"certifyTime\",\"type\":\"uint256\"}],\"selector\":[1463624179,3797287393],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"workId\",\"type\":\"uint256\"},{\"internalType\":\"string\",\"name\":\"fileHash\",\"type\":\"string\"},{\"internalType\":\"address\",\"name\":\"author\",\"type\":\"address\"}],\"name\":\"registerWork\",\"outputs\":[],\"selector\":[1616040735,2958130164],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_CONFIRMCOPYRIGHT = "confirmCopyright";

    public static final String FUNC_GETWORK = "getWork";

    public static final String FUNC_REGISTERWORK = "registerWork";

    protected CopyrightCert(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public TransactionReceipt confirmCopyright(BigInteger workId) {
        final Function function = new Function(
                FUNC_CONFIRMCOPYRIGHT,
                Arrays.<Type>asList(new Uint256(workId)),
                Collections.<TypeReference<?>>emptyList(), 4);
        return executeTransaction(function);
    }

    public Function getMethodConfirmCopyrightRawFunction(BigInteger workId) throws
            ContractException {
        final Function function = new Function(FUNC_CONFIRMCOPYRIGHT,
                Arrays.<Type>asList(new Uint256(workId)),
                Arrays.<TypeReference<?>>asList());
        return function;
    }

    public String getSignedTransactionForConfirmCopyright(BigInteger workId) {
        final Function function = new Function(
                FUNC_CONFIRMCOPYRIGHT,
                Arrays.<Type>asList(new Uint256(workId)),
                Collections.<TypeReference<?>>emptyList(), 4);
        return createSignedTransaction(function);
    }

    public String confirmCopyright(BigInteger workId, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CONFIRMCOPYRIGHT,
                Arrays.<Type>asList(new Uint256(workId)),
                Collections.<TypeReference<?>>emptyList(), 4);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple1<BigInteger> getConfirmCopyrightInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CONFIRMCOPYRIGHT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<BigInteger>(

                (BigInteger) results.get(0).getValue()
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

    public Function getMethodGetWorkRawFunction(BigInteger workId) throws ContractException {
        final Function function = new Function(FUNC_GETWORK,
                Arrays.<Type>asList(new Uint256(workId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        return function;
    }

    public TransactionReceipt registerWork(BigInteger workId, String fileHash, String author) {
        final Function function = new Function(
                FUNC_REGISTERWORK,
                Arrays.<Type>asList(new Uint256(workId),
                new Utf8String(fileHash),
                new Address(author)),
                Collections.<TypeReference<?>>emptyList(), 4);
        return executeTransaction(function);
    }

    public Function getMethodRegisterWorkRawFunction(BigInteger workId, String fileHash,
            String author) throws ContractException {
        final Function function = new Function(FUNC_REGISTERWORK,
                Arrays.<Type>asList(new Uint256(workId),
                new Utf8String(fileHash),
                new Address(author)),
                Arrays.<TypeReference<?>>asList());
        return function;
    }

    public String getSignedTransactionForRegisterWork(BigInteger workId, String fileHash,
            String author) {
        final Function function = new Function(
                FUNC_REGISTERWORK,
                Arrays.<Type>asList(new Uint256(workId),
                new Utf8String(fileHash),
                new Address(author)),
                Collections.<TypeReference<?>>emptyList(), 4);
        return createSignedTransaction(function);
    }

    public String registerWork(BigInteger workId, String fileHash, String author,
            TransactionCallback callback) {
        final Function function = new Function(
                FUNC_REGISTERWORK,
                Arrays.<Type>asList(new Uint256(workId),
                new Utf8String(fileHash),
                new Address(author)),
                Collections.<TypeReference<?>>emptyList(), 4);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple3<BigInteger, String, String> getRegisterWorkInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_REGISTERWORK,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple3<BigInteger, String, String>(

                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue()
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
}
