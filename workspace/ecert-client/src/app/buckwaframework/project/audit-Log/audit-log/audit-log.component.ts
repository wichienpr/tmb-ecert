import { Component, OnInit } from '@angular/core';
import { formatter, TextDateTH } from '../../../common/\u0E49helper/datepicker';
declare var $: any;
@Component({
  selector: 'app-audit-log',
  templateUrl: './audit-log.component.html',
  styleUrls: ['./audit-log.component.css']
})
export class AuditLogComponent implements OnInit {
  showData: boolean = false;  

  action: string[];
  selectaction: string;

   modal: string[] = ['cause'];
  constructor() { 
     this.action = [
      "ทั้งหมด",
      " เข้าใช้งานระบบ",
      "ยื่นใบคำขอสำหรับลูกค้าทำรายการเอง ",
      "ยื่นใบคำขอสำหรับทำรายการให้ลูกค้าลงนาม ",
      " ดำเนินการชำระเงิน/อนุมัติการชำระเงิน",
      " ปฏิเสธการชำระ",
      " ปฏิเสธคำขอ  ",
      " ยกเลิกคำขอ  ",
      " พิมพ์ใบเสร็จ ",
      "พิมพ์ Cover Sheet",
      "Upload Certificate",
      " ดาวน์โหลดใบคำขอหนังสือรับรองนิติบุคคลและหนังสือยินยอมให้หักเงินจากบัญชีเงินฝาก",
      " ดาวน์โหลดสำเนาบัตรประชาชน/ดาวน์โหลดสำเนาใบเปลี่ยนชื่อหรือนามสกุล ",
      " ดาวน์โหลดเอกสาร Certificate", 
  ];
  ;

}

  ngOnInit() {
    $("#calendar1").calendar({
      maxDate: new Date(),
      type: "date",
      text: TextDateTH,
      formatter: formatter()
    });
    $("#calendar2").calendar({
      maxDate: new Date(),
      type: "date",
      text: TextDateTH,
      formatter: formatter()
    });


  }


  ngAfterViewInit() {

  }
  searchData(): void {
    this.showData = true;
    setTimeout(() => {
      $("#table").DataTable({
        scrollX: true,
        searching: false,
        orderable : false,             
        "columnDefs": [{
          "targets": [],
          "orderable": false
        }]
      });
    });

  }

  clearData(): void {
    this.showData = false;
  }

  Onselectaction = event => {
    this.selectaction = this.action[event.target.value];
  };





}

