package com.blockchain.aigc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.sdk.abi.FunctionReturnDecoder;
import org.fisco.bcos.sdk.abi.TypeReference;
import org.fisco.bcos.sdk.abi.datatypes.Address;
import org.fisco.bcos.sdk.abi.datatypes.Event;
import org.fisco.bcos.sdk.abi.datatypes.Function;
import org.fisco.bcos.sdk.abi.datatypes.Type;
import org.fisco.bcos.sdk.abi.datatypes.Utf8String;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple1;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.contract.Contract;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.eventsub.EventCallback;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.model.callback.TransactionCallback;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;

@SuppressWarnings("unchecked")
public class AddressAction extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b506101ae806100206000396000f300608060405260043610610041576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680635aa59c7814610046575b600080fd5b34801561005257600080fd5b506100ad600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506100af565b005b7fe707a97acc29bd17829c5918334e6444fb1e421a9d4450a84d872f20508f2de13382604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610144578082015181840152602081019050610129565b50505050905090810190601f1680156101715780820380516001836020036101000a031916815260200191505b50935050505060405180910390a1505600a165627a7a72305820a475f44be805226995f5da1ceacfe53b37440613e9d7a6b93bc0401afeb5f07b0029"};

    public static final String BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", BINARY_ARRAY);

    public static final String[] SM_BINARY_ARRAY = {"608060405234801561001057600080fd5b506101ae806100206000396000f300608060405260043610610041576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680639a8fdb2214610046575b600080fd5b34801561005257600080fd5b506100ad600480360381019080803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506100af565b005b7f6516c355f7e50a6d5e8c1f35960b726fede285ae09d983dab8be34f91ac0abec3382604051808373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001828103825283818151815260200191508051906020019080838360005b83811015610144578082015181840152602081019050610129565b50505050905090810190601f1680156101715780820380516001836020036101000a031916815260200191505b50935050505060405180910390a1505600a165627a7a723058205af37874fee694d47c19d53a46838f180ee94c71877852b927a1749a45cfce390029"};

    public static final String SM_BINARY = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", SM_BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"action\",\"type\":\"string\"}],\"name\":\"doAction\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"user\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"action\",\"type\":\"string\"}],\"name\":\"ActionDone\",\"type\":\"event\"}]"};

    public static final String ABI = org.fisco.bcos.sdk.utils.StringUtils.joinAll("", ABI_ARRAY);

    public static final String FUNC_DOACTION = "doAction";

    public static final Event ACTIONDONE_EVENT = new Event("ActionDone",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}));
    ;

    protected AddressAction(String contractAddress, Client client, CryptoKeyPair credential) {
        super(getBinary(client.getCryptoSuite()), contractAddress, client, credential);
    }

    public static String getBinary(CryptoSuite cryptoSuite) {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BINARY : SM_BINARY);
    }

    public TransactionReceipt doAction(String action) {
        final Function function = new Function(
                FUNC_DOACTION,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(action)),
                Collections.<TypeReference<?>>emptyList());
        return executeTransaction(function);
    }

    public byte[] doAction(String action, TransactionCallback callback) {
        final Function function = new Function(
                FUNC_DOACTION,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(action)),
                Collections.<TypeReference<?>>emptyList());
        return asyncExecuteTransaction(function, callback);
    }

    public String getSignedTransactionForDoAction(String action) {
        final Function function = new Function(
                FUNC_DOACTION,
                Arrays.<Type>asList(new org.fisco.bcos.sdk.abi.datatypes.Utf8String(action)),
                Collections.<TypeReference<?>>emptyList());
        return createSignedTransaction(function);
    }

    public Tuple1<String> getDoActionInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_DOACTION,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());
        return new Tuple1<String>(

                (String) results.get(0).getValue()
        );
    }

    public List<ActionDoneEventResponse> getActionDoneEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ACTIONDONE_EVENT, transactionReceipt);
        ArrayList<ActionDoneEventResponse> responses = new ArrayList<ActionDoneEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ActionDoneEventResponse typedResponse = new ActionDoneEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.user = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.action = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void subscribeActionDoneEvent(String fromBlock, String toBlock, List<String> otherTopics, EventCallback callback) {
        String topic0 = eventEncoder.encode(ACTIONDONE_EVENT);
        subscribeEvent(ABI,BINARY,topic0,fromBlock,toBlock,otherTopics,callback);
    }

    public void subscribeActionDoneEvent(EventCallback callback) {
        String topic0 = eventEncoder.encode(ACTIONDONE_EVENT);
        subscribeEvent(ABI,BINARY,topic0,callback);
    }

    public static AddressAction load(String contractAddress, Client client, CryptoKeyPair credential) {
        return new AddressAction(contractAddress, client, credential);
    }

    public static AddressAction deploy(Client client, CryptoKeyPair credential) throws ContractException {
        return deploy(AddressAction.class, client, credential, getBinary(client.getCryptoSuite()), "");
    }

    public static class ActionDoneEventResponse {
        public TransactionReceipt.Logs log;

        public String user;

        public String action;
    }
}
