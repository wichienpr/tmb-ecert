import { Component, OnInit } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-client-signed',
  templateUrl: './client-signed.component.html',
  styleUrls: ['./client-signed.component.css']
})
export class ClientSignedComponent implements OnInit {
  products: String[];
  paid: String[];
  customerSegment : String[];
  selectcustomerSegment : String;

  debitMethod: String[];
  showData: boolean = false;
  modal: string[] = ["modal5","modal5"];
  selectProduct: String;
  selectpaid: String;
  selectdebitMethod: String;
  constructor() {
    this.products = [
      "กรุณาเลือก",
      "ห้างหุ้นส่วนจำกัด บริษัทจำกัดและบริษัทมหาชนจำกัด",
      "การประกอบธุรกิจของคนต่างด้าว",
      "สมาคมและหอการค้า",
    ];

    this.paid = ["กรุณาเลือก",
      "ตัดบัญชี (Saving , Curr)", "ลูกค้าชำระค่าธรรมเนียม DBD,TMB","ลูกค้าชำระค่าธรรมเนียม DBD ยกเว้น TMB"];

    this.debitMethod = ["กรุณาเลือก",
      "ธนาคาร (กลุ่มรายย่อย)","ธนาคาร (กลุ่ม SE)","ธนาคาร (กลุ่ม CB/MB/BB)"];
      this.customerSegment =["กรุณาเลือก","BB","CB","Etc","MB","Retail","SE","Legil"]
  }
    


  ngOnInit() {
    //$('#table1').DataTable();
    $('#table1').DataTable({
      searching: false,
    
      scrollX: true
    });
  }

  searchData(): void {
    this.showData = true;
    $('#table1').DataTable({
      ordering: false,
      searching: false,     
      scrollX: true
    });
  }

  clearData(): void {
    this.showData = false;
  }

  onSelectProducts = event => {
    this.selectProduct = this.products[event.target.value];
  };
  onSelectpaid = event => {
    this.selectdebitMethod = this.paid[event.target.value];
  };

  onSelectdebitMethod = event => {
    this.selectpaid = this.debitMethod[event.target.value];
  };
  onselectcustomerSegment = event => {
    this. selectcustomerSegment = this.customerSegment[event.target.value];
  };

  openModal(id) {
    $('#' + id).modal('show');
  }

  closeModal(id) {
    $('#' + id).modal('hide');
  }




}
