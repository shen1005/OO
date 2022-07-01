public class OpNode extends Node {
    private char op;

    public Expr cal() {
        switch (this.op) {
            case '+':
                return this.getLeft().getValue().add(this.getRight().getValue());
            case '-':
                return this.getLeft().getValue().sub(this.getRight().getValue());
            case '*':
                return this.getLeft().getValue().mul(this.getRight().getValue());
            default:
                return null;
        }
    }

    @Override
    public Expr getValue() {
        return this.cal();
    }

    public char getOp() {
        return op;
    }

    public void setOp(char op) {
        this.op = op;
    }
}
