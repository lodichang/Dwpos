package com.dw.entity;

import com.dw.util.AppUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MathBestItemPrice {
    private String itemCode;
    private double amt;
    private double combAmt;
    private double gapAmt;
    private int index;
    private boolean flag;

    public static void main(String[] args) {
        List<MathBestItemPrice> tableView2DtoList = new ArrayList<>();
        //模拟A
        MathBestItemPrice tableView2DtoA = new MathBestItemPrice();
        tableView2DtoA.setItemCode("1001");
        tableView2DtoA.setAmt(42);
        tableView2DtoList.add(tableView2DtoA);
        MathBestItemPrice tableView2DtoB = new MathBestItemPrice();
        tableView2DtoB.setItemCode("1002");
        tableView2DtoB.setAmt(40);
        tableView2DtoList.add(tableView2DtoB);
        MathBestItemPrice tableViewDtC = new MathBestItemPrice();
        tableViewDtC.setItemCode("1003");
        tableViewDtC.setAmt(32);
        tableView2DtoList.add(tableViewDtC);
        //模拟B
        MathBestItemPrice tableViewDt = new MathBestItemPrice();
        tableViewDt.setItemCode("1004");
        tableViewDt.setAmt(32);
        tableView2DtoList.add(tableViewDt);

        MathBestItemPrice tableViewDtoE = new MathBestItemPrice();
        tableViewDtoE.setItemCode("1005");
        tableViewDtoE.setAmt(30);
        tableView2DtoList.add(tableViewDtoE);

        MathBestItemPrice tableViewDtoF = new MathBestItemPrice();
        tableViewDtoF.setItemCode("1006");
        tableViewDtoF.setAmt(28);
        tableView2DtoList.add(tableViewDtoF);

        Map<String, PosComb> combMap = new HashMap<>();
        PosComb posCombA = new PosComb();
        posCombA.setMItemCode("1001");
        posCombA.setVItemCode("1004");
        posCombA.setVItemPrice(new BigDecimal(3));
        combMap.put("10011004", posCombA); //3:77,0:74

        PosComb posCombB = new PosComb();
        posCombB.setMItemCode("1001");
        posCombB.setVItemCode("1005");
        posCombB.setVItemPrice(BigDecimal.ZERO);
        combMap.put("10011005", posCombB);//0:72,3:75

        PosComb posCombC = new PosComb();
        posCombC.setMItemCode("1002");
        posCombC.setVItemCode("1004");
        posCombC.setVItemPrice(new BigDecimal(3));
        combMap.put("10021004", posCombC);//0:72,3:75

        PosComb posCombD = new PosComb();
        posCombD.setMItemCode("1002");
        posCombD.setVItemCode("1005");
        posCombD.setVItemPrice(BigDecimal.ZERO);
        combMap.put("10021005", posCombD);//0:72,3:75

        PosComb posCombE = new PosComb();
        posCombE.setMItemCode("1001");
        posCombE.setVItemCode("1006");
        posCombE.setVItemPrice(new BigDecimal(17));
        combMap.put("10011006", posCombE);//0:72,3:75

        PosComb posCombF = new PosComb();
        posCombF.setMItemCode("1003");
        posCombF.setVItemCode("1004");
        posCombF.setVItemPrice(BigDecimal.ZERO);
        combMap.put("10031004", posCombF);//0:72,3:75

        //1.tableView2DtoList的LIST则为LISTVIEW取代,并需过滤掉没有COMB属性的,以提供匹配性能
        for (int i = 0; i < tableView2DtoList.size(); i++) {
            List<MathBestItemPrice> tvList = new ArrayList<>();
            for (int j = 0; j < tableView2DtoList.size(); j++) {
                //2.匹配组别内的每个元素,组合成一个KEY然后去匹配COMB(组合)里面的,如果有值则证明存在组合.
                //3.然后将所有匹配到的组合放进去一个LIST里面去求GAPAMT的最大值,

                PosComb pc = combMap.get(tableView2DtoList.get(i).getItemCode() + tableView2DtoList.get(j).getItemCode());

                if (AppUtils.isNotBlank(pc) && !tableView2DtoList.get(j).isFlag()) {
                    //System.out.println("M:" + tableView2DtoList.get(i).getItemCode() + " :" + tableView2DtoList.get(j).getItemCode());
                    MathBestItemPrice tv = new MathBestItemPrice();
                    tv.setItemCode(tableView2DtoList.get(j).getItemCode());
                    tv.setAmt(tableView2DtoList.get(j).getAmt());
                    tv.setCombAmt(pc.getVItemPrice().doubleValue());
                    tv.setGapAmt(tv.getAmt() - tv.getCombAmt());
                    tv.setIndex(j);
                    tvList.add(tv);
                }
            }
            //4.然后具有最大GAPAMT的对象则为最优的匹配结果,然后更新LISTVIEW里面的值
            if (tvList.size() > 0) {
                MathBestItemPrice bestMatch = tvList.stream().max(Comparator.comparing(MathBestItemPrice::getGapAmt)).get();
                //4.1更新已组合标记,这样下面的组合就不会匹配到这个编号
                tableView2DtoList.get(bestMatch.getIndex()).setFlag(true);
                //4.2更新B边的售价为组合价
                tableView2DtoList.get(bestMatch.getIndex()).setAmt(bestMatch.getCombAmt());
                System.out.println("A:" + tableView2DtoList.get(i).getItemCode() + "金额:" + tableView2DtoList.get(i).getAmt() + "+B:" + bestMatch.getItemCode() +
                        ",组合价" + bestMatch.getCombAmt() + "=组合金额" + (tableView2DtoList.get(i).getAmt() + bestMatch.getCombAmt()));

            } else if (!tableView2DtoList.get(i).isFlag()) {
                System.out.println("未组合编号:" + tableView2DtoList.get(i).getItemCode() + ",金额:" + tableView2DtoList.get(i).getAmt());
            }

        }

        double sum = tableView2DtoList.stream().collect(Collectors.summarizingDouble(MathBestItemPrice::getAmt)).getSum();
        System.out.println("最终金额:" + sum);


    }
}
