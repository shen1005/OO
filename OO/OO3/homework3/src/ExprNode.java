public class ExprNode extends Node {
    private Expr expr;

    @Override
    public Expr getValue() {
        return this.expr;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }
}
