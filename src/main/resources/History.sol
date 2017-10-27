
pragma solidity ^0.4.16;

contract HistoryContract {

    address public owner;

    // Set the owner of the contract to be the creator of the contract i.e. you
    function HistoryContract() {
        owner = msg.sender;
    }

    // This is an event
    event DepositMade(address _from, uint value);
    event WithdrawalMade(address _to, uint value);

    //Catch all function
    function() payable {
        // generate an event when someone sends you Eth
        if (msg.value > 0) {

            DepositMade(msg.sender, msg.value);

        }
    }

    // Only the owner of the site can withdraw Eth
    modifier admin {
        require(msg.sender == owner);
        _;
    }

    function withdraw(uint amount,address toAddress) admin {


        require(this.balance >= amount);
        require(toAddress.send(amount));

    }

}