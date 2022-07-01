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

    public boolean equals(Expr other) {
        if (this.factors.size() == 0 && other.factors.size() == 0) {
            return true;
        }
        else if (this.factors.size() == 0 || other.factors.size() == 0) {
            return false;
        }
        else {
            boolean flag1 = true;
            for (Factor i : this.factors) {
                boolean flag2 = false;
                for (Factor j: other.factors) {
                    if (i.isAllSame(j)) {
                        flag2 = true;
                    }
                }
                flag1 = flag1 && flag2;
            }
            return flag1  && this.factors.size() == other.factors.size();
        }
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
        return newExpr;
    }

    public Expr mul(Expr expr) {
        Expr newExpr = new Expr();
        for (Factor i: this.getFactors()) {
            for (Factor j: expr.getFactors()) {
                Factor newFactor = new Factor();
                newFactor.setRatio(i.getRatio().multiply(j.getRatio()));
                newFactor.setIndex(i.getIndex().add(j.getIndex()));
                newFactor.setSinCos(i.sinMul(j));
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
                if (this.getFactors().get(i).isSame(j)) {
                    flag = 0;
                    break;
                }
            }
            if (flag == 1) {
                Factor factor = new Factor();
                factor.setRatio(this.getFactors().get(i).getRatio());
                factor.setIndex(this.getFactors().get(i).getIndex());
                factor.setSinCos(this.getFactors().get(i).getSinCos());
                for (int j = i + 1; j < this.factors.size(); j++) {
                    if (this.getFactors().get(i).isSame(this.getFactors().get(j))) {
                        factor.simAdd(this.factors.get(j));
                    }
                }
                expr.addFactor(factor);
            }
        }
        for (int i = 0; i < expr.getFactors().size(); i++) {
            for (int j = i + 1; j < expr.getFactors().size(); j++) {
                if (expr.factors.get(i).isComplement(expr.factors.get(j))) {
                    Factor temp1 = expr.factors.get(i);
                    Factor temp2 = expr.factors.get(j);
                    Factor temp3;
                    if (temp1.getRatio().compareTo(new BigInteger("0")) > 0 &&
                            temp1.getRatio().compareTo(temp2.getRatio()) > 0) {
                        temp3 = temp1;
                        temp1 = temp2;
                        temp2 = temp3;
                    } else if (temp1.getRatio().compareTo(new BigInteger("0")) < 0 &&
                            temp1.getRatio().compareTo(temp2.getRatio()) < 0) {
                        temp3 = temp1;
                        temp1 = temp2;
                        temp2 = temp3;
                    }
                    Factor factor = new Factor();
                    factor.setRatio(temp1.getRatio());
                    factor.setIndex(new BigInteger("0"));
                    expr.addFactor(factor);
                    temp2.setRatio(temp2.getRatio().subtract(temp1.getRatio()));
                    temp1.setRatio(new BigInteger("0"));
                }
            }
        }
        sim2(expr);
        this.setFactors(expr.getFactors());
        this.factors.removeIf(i -> i.getRatio().equals(new BigInteger("0")));
    }

    public void sim2(Expr expr) {
        Factor factor = new Factor();
        factor.setIndex(new BigInteger("0"));
        factor.setRatio(new BigInteger("0"));
        for (Factor i: expr.factors) {
            if (i.getIndex().equals(new BigInteger("0")) && i.getSinCos().size() == 0) {
                factor.simAdd(i);
                i.setRatio(new BigInteger("0"));
            }
        }
        expr.addFactor(factor);
    }
}
