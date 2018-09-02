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
      "หัก บช.ลูกค้า/เงินฝาก หรือกระแสรายวัน", "บัญชีของธนาคาร TMB"];

    this.debitMethod = ["กรุณาเลือก",
      "ธนาคารกุล่ม SE"];
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

  openModal(id) {
    $('#' + id).modal('show');
  }

  closeModal(id) {
    $('#' + id).modal('hide');
  }




}
