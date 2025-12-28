pragma solidity ^0.4.25;

/**
 * @title AddressAction
 * @dev 用户在链上执行一次行为，地址来自 msg.sender
 */
contract AddressAction {

    event ActionDone(address user, string action);

    function doAction(string action) public {
        emit ActionDone(msg.sender, action);
    }
}
