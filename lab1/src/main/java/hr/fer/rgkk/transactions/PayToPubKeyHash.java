package hr.fer.rgkk.transactions;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.crypto.TransactionSignature;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;

import static org.bitcoinj.script.ScriptOpCodes.*;

public class PayToPubKeyHash extends ScriptTransaction {

    // Key used to create destination address of a transaction.
    private final ECKey destinationKey;

    public PayToPubKeyHash(WalletKit walletKit, NetworkParameters parameters) {
        super(walletKit, parameters);
        // Get random destination
        destinationKey = new ECKey();
    }

    @Override
    public Script createLockingScript() {
        return new ScriptBuilder()
                .op(OP_DUP)         // Duplicate public key on stack
                .op(OP_HASH160)     // Replace public key on stack with its hash
                .data(destinationKey.getPubKeyHash())   // Put your public key hash on stack
                .op(OP_EQUALVERIFY) // Check if two key hashes are the same, fail if they are not
                .op(OP_CHECKSIG)    // Now that we know the public key is valid, check the signature with key
                .build();
    }

    @Override
    public Script createUnlockingScript(Transaction unsignedTransaction) {
        TransactionSignature txSig = sign(unsignedTransaction, destinationKey);
        return new ScriptBuilder()
                .data(txSig.encodeToBitcoin())      // Push signature to stack
                .data(destinationKey.getPubKey())   // Push public key to stack
                .build();
    }
}
