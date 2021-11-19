package hr.fer.rgkk.transactions;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;

import static org.bitcoinj.script.ScriptOpCodes.*;

public class LogicalEquivalenceTransaction extends ScriptTransaction {

    public LogicalEquivalenceTransaction(WalletKit walletKit, NetworkParameters parameters) {
        super(walletKit, parameters);
    }

    @Override
    public Script createLockingScript() {
        return new ScriptBuilder()
                .op(OP_2DUP)    // Duplicate last two elements on stack (x and y)
                .number(0)      // Set min of accepted range
                .number(2)      // Set max of accepted range
                .op(OP_WITHIN)  // Check if y is in within range [0, 2>, put 1 on stack if it is, else 0
                .op(OP_VERIFY)  // Verify that 1 is on stack or fail, pop 1 from stack
                .number(0)      // Set min of accepted range
                .number(2)      // Set max of accepted range
                .op(OP_WITHIN)  // Check if x is in within range [0, 2>, put 1 on stack if it is, else 0
                .op(OP_VERIFY)  // Verify that 1 is on stack or fail, pop 1 from stack
                .op(OP_EQUAL)   // Check that x and y are equal (pushes 0 or 1 to stack)
                .build();
    }

    @Override
    public Script createUnlockingScript(Transaction unsignedScript) {
        long x = 1;
        long y = 1;
        return new ScriptBuilder()
                .number(x)
                .number(y)
                .build();
    }
}
