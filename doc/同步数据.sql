#获取tb_pos_comb
SELECT
  r.ID,
  CURRENT_TIMESTAMP                  VERSION,
  '74ece79e580148aeaec52645a5745fed' OUTLINE,
  main_id           AS               M_ITEM_CODE,
  sub_id            AS               V_ITEM_CODE,
  1                 AS               STATUS,
  '0000001'         AS               LAST_UPDATE_NAME_ID,
  CURRENT_TIMESTAMP AS               LAST_UPDATE_TIME,
  l.desccn          AS               COMB_GROUP,
  sub_price         AS               V_ITEM_PRICE
FROM itemlevel_rule r
  left JOIN itemlevel l ON r.ID = l.id;
#同步tb_pos_category
SELECT
  '8888' AS id,
  CURRENT_TIMESTAMP                           VERSION,
  id                                       AS CAT_CODE,
  '74ece79e580148aeaec52645a5745fed'          OUTLINE,
  RTRIM(desccn)                                   AS DESC1,
  RTRIM(descen)                                   AS DESC2,
  RTRIM(desccn)                                   AS DESC3,
  RTRIM(desccn)                                   AS DESC4,
  RTRIM(desccn)                                   AS PRINT_DESC,
  '001'                                    AS SCAT,
  0                                        AS MAX_DISC,
  0                                        AS AMOUNT1,
  0                                        AS AMOUNT2,
  0                                        AS AMOUNT3,
  ''                                       AS REMARKS,
  ''                                       AS DISC,
  ''                                       AS REMARK1,
  ''                                       AS REMARK2,
  ''                                       AS REMARK3,
  '0000001'                                AS LAST_UPDATE_NAME_ID,
  CURRENT_TIMESTAMP                        AS LAST_UPDATE_TIME,
  NULL                                     AS FIRST_SINGLE,
  NULL                                     AS ADD_SINGLE,
  NULL                                     AS UPDATED_BY
FROM cate
WHERE display = 1;
#tb_pos_topbutton
SELECT id AS id,
  CURRENT_TIMESTAMP                           VERSION,
  '74ece79e580148aeaec52645a5745fjb'  OUTLINE,
  '30010'  OUTLET,
   RTRIM(desccn)                                   AS DESCHK,
  RTRIM(descen)                                   AS DESCCN,
  RTRIM(desccn)                                   AS DESCEN,
  RTRIM(desccn)                                   AS DESCOT,
  RTRIM(desccn)                                   AS PRINT_DESC,
  DISPLAY,
dispord as DISSEQ,
0 as FORECOLOR,
0 as BACKCOLOR,
  '303,302,301' as ACTIVE_PERIOD,
  'B,D,C,A' as ACTIVE_ZONE,
  '2018-05-06 00:00:00' as STARTDATE,
  '2019-05-06 00:00:00' as ENDDATE,
  1 as DAY1,
  1 as DAY2,
  1 as DAY3,
  1 as DAY4,
  1 as DAY5,
  1 as DAY6,
  1 as DAY7,
  1 as HOLIDAY,
  1 as EX_HOLIDAY,
  '' as BUTTON_IMG,
    '0000001'                                AS LAST_UPDATE_NAME_ID,
  CURRENT_TIMESTAMP                        AS LAST_UPDATE_TIME
    FROM cate
WHERE   display = 1 and active = 1;
#同步tb_pos_atthead
SELECT
  '8888' ID,
  CURRENT_TIMESTAMP                        VERSION,
  ID                                       CODE,
  '74ece79e580148aeaec52645a5745fed'       OUTLINE,
  RTRIM(desccn)                                   DESC1,
  RTRIM(descen)                                   DESC2,
    RTRIM(desccn) PRINT_DESC,
  0                                        TYPE,
  NULL                                     REMARKS,
  'TRUE'                                   CAN_CHANGE,
  NULL                                     UPDATED_BY,
  NULL                                     REMARK1,
  NULL                                     REMARK2,
  NULL                                     REMARK3,
  '0000001'         AS                     LAST_UPDATE_NAME_ID,
  CURRENT_TIMESTAMP AS                     LAST_UPDATE_TIME
FROM modifier
WHERE active = 1 AND parent_id = 0;
#同步tb_pos_att
SELECT
  '8888' ID,
  CURRENT_TIMESTAMP                        VERSION,
  parent_id                                A_GROUP,
  '74ece79e580148aeaec52645a5745fed'       OUTLINE,
  id                                       code,
  RTRIM(desccn)                                    DESC1,
  RTRIM(descen)                                    DESC2,
  RTRIM(desccn)  PRINT_DESC,
  'TRUE'                                   bill_print,
  'A'                                      CAL_TYPE,
  0.00                                     CAL_AMOUNT,
  0.00                                     CAL_QTY,
  NULL                                     STK_TYPE,
  NULL                                     ING_TYPE,
  '300101'                                 OUTLET,
  'TRUE'                                   CAN_CHANGE,
  NULL                                     UPDATED_BY,
  NULL                                     REMARK1,
  NULL                                     REMARK2,
  price_more                               REMARK3,
  '0000001'         AS                     LAST_UPDATE_NAME_ID,
  CURRENT_TIMESTAMP AS                     LAST_UPDATE_TIME
FROM modifier
WHERE active = 1 AND parent_id <> 0;
#T同步ITEM
CREATE FUNCTION [dbo].[f_base64_decode]
  (@64 VARCHAR(MAX))
  RETURNS VARBINARY(MAX)
AS BEGIN
  RETURN cast(N'' AS XML).value('xs:base64Binary(sql:variable("@64"))', 'varbinary(max)')
END
GO

SET ARITHABORT ON
SET ARITHIGNORE ON
GO

SELECT
  '8888' AS id,
  CURRENT_TIMESTAMP                           VERSION,
  '74ece79e580148aeaec52645a5745fed'          OUTLINE,
  RTRIM(CODE)                                 ITEM_CODE,
  RTRIM(desccn)                               DESC1,
  RTRIM(desccn)                               DESC2,
  RTRIM(descen)                               DESC3,
  dbo.f_base64_decode(RTRIM(Desc_other))      DESC4,
  RTRIM(desckit)                              PRINT_DESC,
  1                                           TYPE,
  cate_id                                     CAT,
  NULL                                        ITEMTYPE,
  NULL                                        ITEMSET,
  'FALSE'                                     IOPEN,
  'TRUE'                                      SERVICE,
  'TRUE'                                      IPRINT,
 RTRIM(printer)                              PRN,
  NULL                                        ATT_TYPE,
  9                                           ATT_MAX,
  (CASE WHEN mustmodi_id <> ''
    THEN 1
   ELSE 0 END)                                ATT_MIN,
  RTRIM(modi_id)                              CATT,
  ''                                          SLOD_OUT,
  0                                           DISCONT,
  price0                                      PRICE1,
  price0                                      PRICE2,
  price0                                      PRICE3,
  price0                                      PRICE4,
  price0                                      PRICE5,
  price0                                      PRICE6,
  price0                                      PRICE7,
  price0                                      PRICE8,
  price0                                      PRICE9,
  cost                                        COST,
  ''                                          ROOM_SEL,
  ''                                          SUMMARY,
  ''                                          MAX_DISC,
  'FALSE'                                     BILL_DISC,
  ''                                          CAN_FREE,
  ''                                          CAN_CANCEL,
  ''                                          I_DECIMAL,
  ''                                          TAX,
  0                                           FREE_MIN,
  0                                           DEF_STOCK,
  0                                           AUTO_REPRT,
  'TRUE'                                      INCLUDE,
  NULL                                        REMARKS,
  NULL                                        UPDATED_BY,
  NULL                                        REMARK1,
  NULL                                        REMARK2,
  NULL                                        REMARK3,
  ''                                          CLOUD_PRINTER,
  NULL                                        ITEM_IMG,
  NULL                                        POS_IMG,
  '0000001'                                AS LAST_UPDATE_NAME_ID,
  CURRENT_TIMESTAMP                        AS LAST_UPDATE_TIME,
  0                                           BULK_PURCHASE,
  0.00                                        ACCOUNT_AMT,
  '002'                                       ACCOUNT_PAYTYPE,
  'MT'                                        PURCHASE_TYPE,
  'single'                                    VALUATION,
  0                                           COMB,
  level_id                                    COMB_ID
FROM item
WHERE active = 1;
#同步tb_pos_pagecont
SELECT
  '8888' AS id,
  CURRENT_TIMESTAMP                           VERSION,
  cate_id                                     PAGE_INDEX,
  code                                        item_code,
  '0000001'                                AS LAST_UPDATE_NAME_ID,
  CURRENT_TIMESTAMP                        AS LAST_UPDATE_TIME,
  ''                                          BACKCOLOR,
  ''                                          ITEM_STAT,
  ''                                          ITEM_TYPE,
  ''                                          SHOWINFO
FROM item
WHERE active = 1;
#同步tb_pos_reason
SELECT
  '8888' AS id,
  CURRENT_TIMESTAMP                           VERSION,
  id                                          code,
  '74ece79e580148aeaec52645a5745fed'          OUTLINE,
  RTRIM(desccn)                               DESC1,
  RTRIM(descen)                               DESC2,
  RTRIM(desccn)                               DESC3,
  RTRIM(desccn)                               DESC4,
  RTRIM(desccn)                               PRIN_DESC,
  10010000                                 AS TYPE,
  '0000001'                                AS LAST_UPDATE_NAME_ID,
  CURRENT_TIMESTAMP                        AS LAST_UPDATE_TIME
FROM void_reason
#同步tb_pos_attsetting, 本地庫
INSERT INTO tb_pos_attsetting (ID, VERSION, A_GROUP, CODE, ACTION_CODE, CAL_AMOUNT, CAL_QTY, LAST_UPDATE_NAME_ID, LAST_UPDATE_TIME, OUTLINE)
  SELECT
    replace(uuid(), '-', '') AS        ID,
    CURRENT_TIMESTAMP        AS        VERSION,
    t.A_GROUP,
    t.CODE,
    a.ACTION_CODE,
    (CASE WHEN a.ACTION_CODE = 1
      THEN t.REMARK3
     ELSE 0.00 END)          AS        CAL_AMOUNT,
    0                        AS        CAL_QTY,
    '0000001'                AS        LAST_UPDATE_NAME_ID,
    CURRENT_TIMESTAMP        AS        LAST_UPDATE_TIME,
    '74ece79e580148aeaec52645a5745fed' OUTLINE
  FROM tb_pos_att t, tb_pos_attaction a
  WHERE t.A_GROUP IN ('1714', '1786', '981', '1769', '1443', '1038', '1730');

#tb_po_setmeal

--'811','813','814','850'
--'4801','4802','4803','4804'

--1.先查看套餐裡面的mitem有多個
SELECT *
FROM setitem
WHERE id IN ('850');
--2.根據1的mitem加入下面
SELECT id,item_list
FROM mitem
WHERE id IN (SELECT mitem_id
FROM setitem
WHERE id IN ('850'));

SELECT '8888' AS id,
  CURRENT_TIMESTAMP                  VERSION,
  '74ece79e580148aeaec52645a5745fed' OUTLINE,  RTRIM(ig.CODE) code,disporder S_GROUP,qty S_COUNT,RTRIM(i.code) ITEM_CODE,i.priceset ADD_PRICE,
  '0000001'                                AS LAST_UPDATE_NAME_ID,
  CURRENT_TIMESTAMP                        AS LAST_UPDATE_TIME
FROM setitem s LEFT JOIN item ig ON ig.id = s.id
  LEFT JOIN item i ON i.id = s.item_id
WHERE  s.id IN (850) and s.item_id <> 0
UNION ALL
SELECT '8888' AS id,
  CURRENT_TIMESTAMP                  VERSION,
  '74ece79e580148aeaec52645a5745fed' OUTLINE, RTRIM(ig.CODE) code,disporder S_GROUP,qty S_COUNT,RTRIM(i.code) ITEM_CODE,i.priceset ADD_PRICE,
  '0000001'                                AS LAST_UPDATE_NAME_ID,
  CURRENT_TIMESTAMP                        AS LAST_UPDATE_TIME
FROM setitem s LEFT JOIN item ig ON ig.id = s.id
   LEFT JOIN (
    select i.code,i.priceset,20 mitem_id from item i where i.id in (select *  from Split('785,786,',','))
    UNION ALL
    select i.code,i.priceset,24 mitem_id from item i where i.id in (select *  from Split('783,812,',','))
     UNION ALL
    select i.code,i.priceset,25 mitem_id from item i where i.id in (select *  from Split('789,790,791,792,',','))
             ) i ON i.mitem_id = s.mitem_id
WHERE  s.id IN (850) and s.item_id = 0 ;
