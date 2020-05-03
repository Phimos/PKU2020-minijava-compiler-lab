package symbol;

import java.lang.reflect.Array;
import java.util.*;

import java.lang.Math;

public class MSpgProcedure {
    public ArrayList<MSpgStmt> stmts = new ArrayList<MSpgStmt>();
    HashMap<String, MSpgStmt> labels = new HashMap<String, MSpgStmt>();
    ArrayList<String> callMethodName = new ArrayList<>();
    int[][] intervals;
    public Integer paramsCnt = 0;
    public Integer spilledCnt = 0;
    public Integer maxParamsCnt = 0;
    public String tempLabel = null;
    HashMap<Integer, Integer> index = new HashMap<>();
    HashMap<Integer, String> temp2regname = new HashMap<>();

    String[] regs = {"s0","s1","s2","s3","s4","s5","s6","s7"
    ,"t0","t1","t2","t3","t4","t5","t6","t7","t8","t9"
    ,"a0","a1","a2","a3","v0","v1"};
    boolean[] used = new boolean[24];
    HashMap<Integer, Integer> tempRegs = new HashMap<>(); // temp id, reg index

    class MInterval{
        public int val,left,right;
        MInterval(int val,int left,int right){
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    PriorityQueue<MInterval> alloced = new PriorityQueue<MInterval>((a,b)->{return a.right-b.right;});

    public int stmtCnt = -1;

    public void addStmt(MSpgStmt stmt){
        if(tempLabel != null){
            labels.put(tempLabel, stmt);
            this.tempLabel = null;
        }
        stmts.add(stmt);
    }

    public void setParamsCnt(int n){
        paramsCnt = n;
    }

    public void addCall(String name){
        callMethodName.add(name);
    }

    public void analyze(){
        buildSucc();
        boolean nochange = false;
        while(!nochange){
            nochange = true;
            for(int i=stmts.size()-1;i>=0;--i){
                nochange &= stmts.get(i).update();
            }
        }
    }

    public void buildSucc(){
        for(int i=0;i<stmts.size()-1;++i){
            switch(stmts.get(i).type){
                case CJUMP:
                    stmts.get(i).succ.add(stmts.get(i+1));
                    stmts.get(i).succ.add(labels.get(stmts.get(i).jumpLabel));
                    break;
                case JUMP:
                    stmts.get(i).succ.add(labels.get(stmts.get(i).jumpLabel));
                    break;
                default:
                    stmts.get(i).succ.add(stmts.get(i+1));
                    break;
            }
        }
    }

    public void buildIntervals(){
        Set<Integer> allTemp = new HashSet<>();
        for(MSpgStmt stmt: stmts){
            allTemp.addAll(stmt.active);
        }
        int cnt = 0;
        intervals = new int[allTemp.size()][3];
        for(Integer val: allTemp){
            intervals[cnt][0] = val;
            intervals[cnt][1] = stmts.size();
            intervals[cnt][2] = 0;
            index.put(val, cnt++);
        }
        for(int i=0;i<stmts.size();++i){
            for(Integer temp: stmts.get(i).active){
                int idx = index.get(temp);
                intervals[idx][1] = Math.min(intervals[idx][1], i);
                intervals[idx][2] = Math.max(intervals[idx][2], i);
            }
        }

        for(int i=0;i<intervals.length;++i){
            System.out.print(intervals[i][0]);
            System.out.print(" ");
            System.out.print(intervals[i][1]);
            System.out.print(" ");
            System.out.println(intervals[i][2]);
        }

        //for(int i=0;i<intervals.length;++i){
        //    alloced.add(new MInterval(intervals[i][0],intervals[i][1],intervals[i][2]));
        //}
        //System.out.println("????");
        //while(!alloced.isEmpty()){
        //    System.out.println(alloced.peek().right);
        //    alloced.poll();
        //}

        for(int i=0;i<used.length;++i){
            used[i] = false;
        }

        for(Integer val: allTemp){
            if(val < this.paramsCnt){
                if(val < 4){
                    temp2regname.put(val, "s"+val);
                }
                else{
                    temp2regname.put(val, "SPILLEDARG "+(val-4));
                }
            }
        }
        spilledCnt= Math.max(paramsCnt - 4, 0);


        for(int i=0;i<stmts.size();++i){
            if(stmts.get(i).hasCall){
                System.out.println(i);
            }
        }

    }

    public void spillReg(int temp){
        alloced.removeIf((x)->{return x.val == temp;});
        if(tempRegs.containsKey(temp)){
            used[tempRegs.get(temp)] = false;
            tempRegs.remove(temp);
        }
        temp2regname.put(temp, "SPILLEDART " + spilledCnt);
        spilledCnt++;
    }

    public void spillLatest(int t,int temp){
        MInterval top = new MInterval(-1,0,0);
        for(MInterval itv: alloced){
            if(itv.right > top.right){
                top = itv;
            }
        }
        if(top.right <= intervals[index.get(temp)][2]){
            spillReg(temp);
        }
        else{
            spillReg(top.val);
            allocReg(t, temp);
        }
    }

    public void clearReg(int t){
        while(!alloced.isEmpty()&&alloced.peek().right<t){
            MInterval i = alloced.poll();
            used[tempRegs.get(i.val)] = false;
        }
    }

    public void allocReg(int t,int temp){
        clearReg(t);
        for(int i=8;i<18;++i){
            if(!used[i]){
                tempRegs.put(temp, i);
                temp2regname.put(temp, regs[i]);
                used[i]=true;
                int idx = index.get(temp);
                alloced.add(new MInterval(temp,intervals[idx][1], intervals[idx][2]));
                return;
            }
        }
        // spilled
        spillLatest(t, temp);
    }

    public String getReg(int temp){
        if(temp2regname.containsKey(temp))
            return temp2regname.get(temp);
        else{
            allocReg(stmtCnt, temp);
            return temp2regname.get(temp);
        }
    }
}