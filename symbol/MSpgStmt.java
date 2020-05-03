package symbol;
import java.util.*;


public class MSpgStmt {
    ArrayList<MSpgStmt> succ = new ArrayList<MSpgStmt>();
    public Set<Integer> vars = new HashSet<Integer>();
    public Set<Integer> ids = new HashSet<Integer>();
    public Set<Integer> active = new HashSet<Integer>();
    public String jumpLabel = null;
    public StmtType type;
    public boolean hasCall = false;

    public MSpgStmt(StmtType t){
        type = t;
    }

    public void addVar(int i){
        vars.add(i);
    }

    public void addID(int i){
        ids.add(i);
    }

    public void addSucc(MSpgStmt stmt){
        succ.add(stmt);
    }
    
    public void incall(){
        this.hasCall =true;
    }

    public boolean update(){
        Set<Integer> temp = new HashSet<Integer>();
        boolean r;
        temp = join();
        temp.removeAll(ids);
        temp.addAll(vars);
        r = temp.equals(active);
        active = temp;
        return r;
    }

    public Set<Integer> join(){
        Set<Integer> ans = new HashSet<Integer>();
        for(MSpgStmt w: succ){
            ans.addAll(w.active);
        }
        return ans;
    }
}