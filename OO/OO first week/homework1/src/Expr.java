import java.math.BigInteger;
import java.util.ArrayList;

public class Expr {
    private  ArrayList<Factor> factors = new ArrayList<>();

    public void addFactor(Factor factor) {
        this.factors.add(factor);
    }

    public void addFactors(ArrayList<Factor> factors) {
        this.factors.addAll(factors);
    }

    public Expr add(Expr expr) {
        Expr newExpr = new Expr();
        newExpr.addFactors(this.factors);
        newExpr.addFactors(expr.getFactors());
        newExpr.simplify();
        return newExpr;
    }

    public Expr sub(Expr expr) {
        Expr newExpr;
        Expr subExpr;
        subExpr = expr;
        for (Factor i: subExpr.getFactors()) {
            i.setRatio(new BigInteger("0").subtract(i.getRatio()));//将符号变成相反数
        }
        newExpr = this.add(subExpr);
        newExpr.simplify();
        return newExpr;
    }

    public Expr mul(Expr expr) {
        Expr newExpr = new Expr();
        for (Factor i: this.getFactors()) {
            for (Factor j: expr.getFactors()) {
                Factor newFactor = new Factor();
                newFactor.setRatio(i.getRatio().multiply(j.getRatio()));
                newFactor.setIndex(i.getIndex().add(j.getIndex()));
                newExpr.addFactor(newFactor);
            }
        }
        newExpr.simplify();
        return newExpr;
    }

    public ArrayList<Factor> getFactors() {
        return factors;
    }

    public String des() {
        if (this.factors.size() == 0) {
            return "0";
        }
        else {
            String temp = this.factors.get(0).getDes();
            for (int i = 1; i < this.factors.size(); i++) {
                if (this.factors.get(i).getRatio().compareTo(new BigInteger("0")) > 0) {
                    temp = String.format("%s+%s", temp, this.factors.get(i).getDes());
                }
                else {
                    temp = temp + this.factors.get(i).getDes();
                }
            }
            return temp;
        }
    }

    public void setFactors(ArrayList<Factor> factors) {
        this.factors = factors;
    }

    public void simplify() {
        this.getFactors().removeIf(i -> i.getRatio().equals(new BigInteger("0")));
        Expr expr = new Expr();
        int flag;
        for (int i = 0; i < this.getFactors().size(); i++) {
            flag = 1;
            for (Factor j: expr.getFactors()) {
                if (this.getFactors().get(i).getIndex().equals(j.getIndex())) {
                    flag = 0;
                    break;
                }
            }
            if (flag == 1) {
                Factor factor = new Factor();
                factor.setRatio(this.getFactors().get(i).getRatio());
                factor.setIndex(this.getFactors().get(i).getIndex());
                for (int j = i + 1; j < this.factors.size(); j++) {
                    if (this.factors.get(i).getIndex().equals(this.factors.get(j).getIndex())) {
                        factor.simAdd(this.factors.get(j));
                    }
                }
                expr.addFactor(factor);
            }
        }
        this.setFactors(expr.getFactors());
        this.factors.removeIf(i -> i.getRatio().equals(new BigInteger("0")));
    }
}
