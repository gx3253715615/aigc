package com.blockchain.aigc.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.v3.client.Client;
import org.fisco.bcos.sdk.v3.codec.datatypes.Address;
import org.fisco.bcos.sdk.v3.codec.datatypes.DynamicArray;
import org.fisco.bcos.sdk.v3.codec.datatypes.Function;
import org.fisco.bcos.sdk.v3.codec.datatypes.Type;
import org.fisco.bcos.sdk.v3.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.v3.codec.datatypes.Utf8String;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint256;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.Uint8;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple5;
import org.fisco.bcos.sdk.v3.codec.datatypes.generated.tuples.generated.Tuple6;
import org.fisco.bcos.sdk.v3.contract.Contract;
import org.fisco.bcos.sdk.v3.crypto.CryptoSuite;
import org.fisco.bcos.sdk.v3.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.v3.model.CryptoType;
import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.v3.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class CopyrightTransfer extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610a62806100206000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c8063496241491461005c57806362df9111146100715780637ace341b1461009a578063c16fe907146100c5578063c9988e64146100ea575b600080fd5b61006f61006a366004610777565b61010a565b005b61008461007f36600461085d565b6102fc565b6040516100919190610876565b60405180910390f35b6100ad6100a836600461085d565b61035e565b6040516001600160a01b039091168152602001610091565b6100d86100d336600461085d565b610477565b604051610091969594939291906108e4565b6100fd6100f836600461085d565b6105b1565b604051610091919061097d565b600085815260208190526040902060050154156101605760405162461bcd60e51b815260206004820152600f60248201526e7472616e736665722065786973747360881b60448201526064015b60405180910390fd5b600061016b8561035e565b90506001600160a01b038116156101d457336001600160a01b038216146101d45760405162461bcd60e51b815260206004820152601760248201527f6f6e6c79206f776e65722063616e207472616e736665720000000000000000006044820152606401610157565b6040518060e00160405280878152602001868152602001858152602001826001600160a01b03168152602001846001600160a01b03168152602001836002811115610221576102216108ba565b815242602091820152600088815280825260409081902083518155838301516001820155908301518051919261025f9260028501929091019061069d565b5060608201516003820180546001600160a01b03199081166001600160a01b0393841617909155608084015160048401805492831691909316908117835560a085015192916001600160a81b03191617600160a01b8360028111156102c6576102c66108ba565b021790555060c0919091015160059091015550505060009182525060016020818152604083208054928301815583529091200155565b60008181526001602090815260409182902080548351818402810184019094528084526060939283018282801561035257602002820191906000526020600020905b81548152602001906001019080831161033e575b50505050509050919050565b6000818152600160209081526040808320805482518185028101850190935280835284938301828280156103b157602002820191906000526020600020905b81548152602001906001019080831161039d575b505050505090508051600014156103cb5750600092915050565b80515b801561046d5760008080846103e46001866109ad565b815181106103f4576103f46109c4565b6020026020010151815260200190815260200160002090506000600281111561041f5761041f6108ba565b6004820154600160a01b900460ff16600281111561043f5761043f6108ba565b141561045a57600401546001600160a01b0316949350505050565b5080610465816109da565b9150506103ce565b5060009392505050565b600081815260208190526040812060058101546060918391829182918291906104d85760405162461bcd60e51b81526020600482015260136024820152727472616e73666572206e6f742065786973747360681b6044820152606401610157565b600181015460038201546004830154600584015460028501805490936001600160a01b039081169390811692600160a01b90910460ff1691859061051b906109f1565b80601f0160208091040260200160405190810160405280929190818152602001828054610547906109f1565b80156105945780601f1061056957610100808354040283529160200191610594565b820191906000526020600020905b81548152906001019060200180831161057757829003601f168201915b505050505094509650965096509650965096505091939550919395565b60008181526001602090815260408083208054825181850281018501909352808352849383018282801561060457602002820191906000526020600020905b8154815260200190600101908083116105f0575b5050505050905080516000141561061e5750600092915050565b6000806000836001855161063291906109ad565b81518110610642576106426109c4565b6020026020010151815260200190815260200160002090506001600281111561066d5761066d6108ba565b6004820154600160a01b900460ff16600281111561068d5761068d6108ba565b141561046d575060019392505050565b8280546106a9906109f1565b90600052602060002090601f0160209004810192826106cb5760008555610711565b82601f106106e457805160ff1916838001178555610711565b82800160010185558215610711579182015b828111156107115782518255916020019190600101906106f6565b5061071d929150610721565b5090565b5b8082111561071d5760008155600101610722565b634e487b7160e01b600052604160045260246000fd5b80356001600160a01b038116811461076357600080fd5b919050565b80356003811061076357600080fd5b600080600080600060a0868803121561078f57600080fd5b8535945060208601359350604086013567ffffffffffffffff808211156107b557600080fd5b818801915088601f8301126107c957600080fd5b8135818111156107db576107db610736565b604051601f8201601f19908116603f0116810190838211818310171561080357610803610736565b816040528281528b602084870101111561081c57600080fd5b8260208601602083013760006020848301015280975050505050506108436060870161074c565b915061085160808701610768565b90509295509295909350565b60006020828403121561086f57600080fd5b5035919050565b6020808252825182820181905260009190848201906040850190845b818110156108ae57835183529284019291840191600101610892565b50909695505050505050565b634e487b7160e01b600052602160045260246000fd5b600381106108e0576108e06108ba565b9052565b8681526000602060c08184015287518060c085015260005b818110156109185789810183015185820160e0015282016108fc565b8181111561092a57600060e083870101525b50601f01601f1916830160e0019150610950905060408301876001600160a01b03169052565b6001600160a01b038516606083015261096c60808301856108d0565b8260a0830152979650505050505050565b6020810160028310610991576109916108ba565b91905290565b634e487b7160e01b600052601160045260246000fd5b6000828210156109bf576109bf610997565b500390565b634e487b7160e01b600052603260045260246000fd5b6000816109e9576109e9610997565b506000190190565b600181811c90821680610a0557607f821691505b60208210811415610a2657634e487b7160e01b600052602260045260246000fd5b5091905056fea26469706673582212203b6d258b87fe659461ef90b97d8895fbcf7106ab084bcde22c7b7273abac951f64736f6c634300080b0033"};

    public static final String BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b50610a61806100206000396000f3fe608060405234801561001057600080fd5b50600436106100575760003560e01c8063978951ff1461005c578063c2836f421461008c578063cd5e670e146100a1578063d34da303146100c1578063ffee887e146100e6575b600080fd5b61006f61006a366004610735565b610106565b6040516001600160a01b0390911681526020015b60405180910390f35b61009f61009a36600461078f565b61021f565b005b6100b46100af366004610735565b610413565b604051610083919061088b565b6100d46100cf366004610735565b6104ff565b604051610083969594939291906108b9565b6100f96100f4366004610735565b61063a565b6040516100839190610952565b60008181526001602090815260408083208054825181850281018501909352808352849383018282801561015957602002820191906000526020600020905b815481526020019060010190808311610145575b505050505090508051600014156101735750600092915050565b80515b801561021557600080808461018c6001866109ac565b8151811061019c5761019c6109c3565b602002602001015181526020019081526020016000209050600060028111156101c7576101c7610875565b6004820154600160a01b900460ff1660028111156101e7576101e7610875565b141561020257600401546001600160a01b0316949350505050565b508061020d816109d9565b915050610176565b5060009392505050565b6000858152602081905260409020600501541561027657604051636381e58960e11b815260206004820152600f60248201526e7472616e736665722065786973747360881b60448201526064015b60405180910390fd5b600061028185610106565b90506001600160a01b038116156102eb57336001600160a01b038216146102eb57604051636381e58960e11b815260206004820152601760248201527f6f6e6c79206f776e65722063616e207472616e73666572000000000000000000604482015260640161026d565b6040518060e00160405280878152602001868152602001858152602001826001600160a01b03168152602001846001600160a01b0316815260200183600281111561033857610338610875565b81524260209182015260008881528082526040908190208351815583830151600182015590830151805191926103769260028501929091019061069c565b5060608201516003820180546001600160a01b03199081166001600160a01b0393841617909155608084015160048401805492831691909316908117835560a085015192916001600160a81b03191617600160a01b8360028111156103dd576103dd610875565b021790555060c0919091015160059091015550505060009182525060016020818152604083208054928301815583529091200155565b60008181526001602090815260408083208054825181850281018501909352808352849383018282801561046657602002820191906000526020600020905b815481526020019060010190808311610452575b505050505090508051600014156104805750600092915050565b6000806000836001855161049491906109ac565b815181106104a4576104a46109c3565b602002602001015181526020019081526020016000209050600160028111156104cf576104cf610875565b6004820154600160a01b900460ff1660028111156104ef576104ef610875565b1415610215575060019392505050565b6000818152602081905260408120600581015460609183918291829182919061056157604051636381e58960e11b81526020600482015260136024820152727472616e73666572206e6f742065786973747360681b604482015260640161026d565b600181015460038201546004830154600584015460028501805490936001600160a01b039081169390811692600160a01b90910460ff169185906105a4906109f0565b80601f01602080910402602001604051908101604052809291908181526020018280546105d0906109f0565b801561061d5780601f106105f25761010080835404028352916020019161061d565b820191906000526020600020905b81548152906001019060200180831161060057829003601f168201915b505050505094509650965096509650965096505091939550919395565b60008181526001602090815260409182902080548351818402810184019094528084526060939283018282801561069057602002820191906000526020600020905b81548152602001906001019080831161067c575b50505050509050919050565b8280546106a8906109f0565b90600052602060002090601f0160209004810192826106ca5760008555610710565b82601f106106e357805160ff1916838001178555610710565b82800160010185558215610710579182015b828111156107105782518255916020019190600101906106f5565b5061071c929150610720565b5090565b5b8082111561071c5760008155600101610721565b60006020828403121561074757600080fd5b5035919050565b63b95aa35560e01b600052604160045260246000fd5b80356001600160a01b038116811461077b57600080fd5b919050565b80356003811061077b57600080fd5b600080600080600060a086880312156107a757600080fd5b8535945060208601359350604086013567ffffffffffffffff808211156107cd57600080fd5b818801915088601f8301126107e157600080fd5b8135818111156107f3576107f361074e565b604051601f8201601f19908116603f0116810190838211818310171561081b5761081b61074e565b816040528281528b602084870101111561083457600080fd5b82602086016020830137600060208483010152809750505050505061085b60608701610764565b915061086960808701610780565b90509295509295909350565b63b95aa35560e01b600052602160045260246000fd5b602081016002831061089f5761089f610875565b91905290565b600381106108b5576108b5610875565b9052565b8681526000602060c08184015287518060c085015260005b818110156108ed5789810183015185820160e0015282016108d1565b818111156108ff57600060e083870101525b50601f01601f1916830160e0019150610925905060408301876001600160a01b03169052565b6001600160a01b038516606083015261094160808301856108a5565b8260a0830152979650505050505050565b6020808252825182820181905260009190848201906040850190845b8181101561098a5783518352928401929184019160010161096e565b50909695505050505050565b63b95aa35560e01b600052601160045260246000fd5b6000828210156109be576109be610996565b500390565b63b95aa35560e01b600052603260045260246000fd5b6000816109e8576109e8610996565b506000190190565b600181811c90821680610a0457607f821691505b60208210811415610a255763b95aa35560e01b600052602260045260246000fd5b5091905056fea2646970667358221220ca336b9811fd0eafbe8f71ce0542ff45847c55cbd5ddf1b753c24aac2bec6fe464736f6c634300080b0033"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"conflictFields\":[{\"kind\":0},{\"kind\":3,\"slot\":0,\"value\":[0]},{\"kind\":3,\"slot\":1,\"value\":[1]}],\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"transferId\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"workId\",\"type\":\"uint256\"},{\"internalType\":\"string\",\"name\":\"fileHash\",\"type\":\"string\"},{\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"},{\"internalType\":\"enum CopyrightTransfer.TransferType\",\"name\":\"transferType\",\"type\":\"uint8\"}],\"name\":\"createTransfer\",\"outputs\":[],\"selector\":[1231176009,3263393602],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":0},{\"kind\":3,\"slot\":1,\"value\":[0]}],\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"workId\",\"type\":\"uint256\"}],\"name\":\"getCurrentOwner\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"selector\":[2060334107,2542359039],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":0},{\"kind\":3,\"slot\":1,\"value\":[0]}],\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"workId\",\"type\":\"uint256\"}],\"name\":\"getCurrentRightType\",\"outputs\":[{\"internalType\":\"enum CopyrightTransfer.RightType\",\"name\":\"\",\"type\":\"uint8\"}],\"selector\":[3382218340,3445516046],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":0,\"value\":[0]}],\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"transferId\",\"type\":\"uint256\"}],\"name\":\"getTransfer\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"workId\",\"type\":\"uint256\"},{\"internalType\":\"string\",\"name\":\"fileHash\",\"type\":\"string\"},{\"internalType\":\"address\",\"name\":\"from\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"to\",\"type\":\"address\"},{\"internalType\":\"enum CopyrightTransfer.TransferType\",\"name\":\"transferType\",\"type\":\"uint8\"},{\"internalType\":\"uint256\",\"name\":\"timestamp\",\"type\":\"uint256\"}],\"selector\":[3245336839,3545080579],\"stateMutability\":\"view\",\"type\":\"function\"},{\"conflictFields\":[{\"kind\":3,\"slot\":1,\"value\":[0]}],\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"workId\",\"type\":\"uint256\"}],\"name\":\"getWorkTransfers\",\"outputs\":[{\"internalType\":\"uint256[]\",\"name\":\"\",\"type\":\"uint256[]\"}],\"selector\":[1658818833,4293822590],\"stateMutability\":\"view\",\"type\":\"function\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.v3.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_CREATETRANSFER = "createTransfer";

    public static final String FUNC_GETCURRENTOWNER = "getCurrentOwner";

    public static final String FUNC_GETCURRENTRIGHTTYPE = "getCurrentRightType";

    public static final String FUNC_GETTRANSFER = "getTransfer";

    public static final String FUNC_GETWORKTRANSFERS = "getWorkTransfers";

    protected CopyrightTransfer(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public static String getABI() {
        return ABI;
    }

    public TransactionReceipt createTransfer(BigInteger transferId, BigInteger workId,
            String fileHash, String to, BigInteger transferType) {
        final Function function = new Function(
                FUNC_CREATETRANSFER,
                Arrays.<Type>asList(new Uint256(transferId),
                new Uint256(workId),
                new Utf8String(fileHash),
                new Address(to),
                new Uint8(transferType)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return executeTransaction(function);
    }

    public Function getMethodCreateTransferRawFunction(BigInteger transferId, BigInteger workId,
            String fileHash, String to, BigInteger transferType) throws ContractException {
        final Function function = new Function(FUNC_CREATETRANSFER,
                Arrays.<Type>asList(new Uint256(transferId),
                new Uint256(workId),
                new Utf8String(fileHash),
                new Address(to),
                new Uint8(transferType)),
                Arrays.<TypeReference<?>>asList());
        return function;
    }

    public String getSignedTransactionForCreateTransfer(BigInteger transferId, BigInteger workId,
            String fileHash, String to, BigInteger transferType) {
        final Function function = new Function(
                FUNC_CREATETRANSFER,
                Arrays.<Type>asList(new Uint256(transferId),
                new Uint256(workId),
                new Utf8String(fileHash),
                new Address(to),
                new Uint8(transferType)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return createSignedTransaction(function);
    }

    public String createTransfer(BigInteger transferId, BigInteger workId, String fileHash,
            String to, BigInteger transferType, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_CREATETRANSFER,
                Arrays.<Type>asList(new Uint256(transferId),
                new Uint256(workId),
                new Utf8String(fileHash),
                new Address(to),
                new Uint8(transferType)),
                Collections.<TypeReference<?>>emptyList(), 0);
        return asyncExecuteTransaction(function, callback);
    }

    public Tuple5<BigInteger, BigInteger, String, String, BigInteger> getCreateTransferInput(
            TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_CREATETRANSFER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Uint8>() {}));
        List<Type> results = this.functionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple5<BigInteger, BigInteger, String, String, BigInteger>(

                (BigInteger) results.get(0).getValue(),
                (BigInteger) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (BigInteger) results.get(4).getValue()
                );
    }

    public String getCurrentOwner(BigInteger workId) throws ContractException {
        final Function function = new Function(FUNC_GETCURRENTOWNER,
                Arrays.<Type>asList(new Uint256(workId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeCallWithSingleValueReturn(function, String.class);
    }

    public Function getMethodGetCurrentOwnerRawFunction(BigInteger workId) throws
            ContractException {
        final Function function = new Function(FUNC_GETCURRENTOWNER,
                Arrays.<Type>asList(new Uint256(workId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return function;
    }

    public BigInteger getCurrentRightType(BigInteger workId) throws ContractException {
        final Function function = new Function(FUNC_GETCURRENTRIGHTTYPE,
                Arrays.<Type>asList(new Uint256(workId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeCallWithSingleValueReturn(function, BigInteger.class);
    }

    public Function getMethodGetCurrentRightTypeRawFunction(BigInteger workId) throws
            ContractException {
        final Function function = new Function(FUNC_GETCURRENTRIGHTTYPE,
                Arrays.<Type>asList(new Uint256(workId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return function;
    }

    public Tuple6<BigInteger, String, String, String, BigInteger, BigInteger> getTransfer(
            BigInteger transferId) throws ContractException {
        final Function function = new Function(FUNC_GETTRANSFER,
                Arrays.<Type>asList(new Uint256(transferId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint256>() {}));
        List<Type> results = executeCallWithMultipleValueReturn(function);
        return new Tuple6<BigInteger, String, String, String, BigInteger, BigInteger>(
                (BigInteger) results.get(0).getValue(),
                (String) results.get(1).getValue(),
                (String) results.get(2).getValue(),
                (String) results.get(3).getValue(),
                (BigInteger) results.get(4).getValue(),
                (BigInteger) results.get(5).getValue());
    }

    public Function getMethodGetTransferRawFunction(BigInteger transferId) throws
            ContractException {
        final Function function = new Function(FUNC_GETTRANSFER,
                Arrays.<Type>asList(new Uint256(transferId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint256>() {}));
        return function;
    }

    public List getWorkTransfers(BigInteger workId) throws ContractException {
        final Function function = new Function(FUNC_GETWORKTRANSFERS,
                Arrays.<Type>asList(new Uint256(workId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        List<Type> result = (List<Type>) executeCallWithSingleValueReturn(function, List.class);
        return convertToNative(result);
    }

    public Function getMethodGetWorkTransfersRawFunction(BigInteger workId) throws
            ContractException {
        final Function function = new Function(FUNC_GETWORKTRANSFERS,
                Arrays.<Type>asList(new Uint256(workId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {}));
        return function;
    }

    public static CopyrightTransfer load(String contractAddress, Client client,
            CryptoKeyPair credential) {
        return new CopyrightTransfer(contractAddress, client, credential);
    }

    public static CopyrightTransfer deploy(Client client, CryptoKeyPair credential) throws
            ContractException {
        return deploy(CopyrightTransfer.class, client, credential, getBinary(client.getCryptoSuite()), getABI(), null, null);
    }
}
