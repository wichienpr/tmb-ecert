export enum REQ_STATUS {
    ST10001 = "10001", //คำขอใหม่
    ST10002 = "10002", //ดำเนินการชำระเงิน
    ST10003 = "10003", //ปฏิเสธคำขอ
    ST10004 = "10004", //ยกเลิกคำขอ
    ST10005 = "10005", //รอนุมัติการชำระเงิน
    ST10006 = "10006", //อนุมัติการชำระเงิน
    ST10007 = "10007", //ปฏิเสธการชำระเงิน
    ST10008 = "10008", //ชำระเงินไม่สำเร็จ
    ST10009 = "10009", //รออัพโหลด Certificate
    ST10010 = "10010", //ดำเนินการสำเร็จ
    ST10011 = "10011", //รอบันทึกคำขอ (ลูกค้าทำรายการเอง)
}