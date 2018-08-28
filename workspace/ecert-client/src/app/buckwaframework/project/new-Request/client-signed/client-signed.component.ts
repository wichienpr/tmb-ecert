import { Component, OnInit } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-client-signed',
  templateUrl: './client-signed.component.html',
  styleUrls: ['./client-signed.component.css']
})
export class ClientSignedComponent implements OnInit {
  products: String[];  
  showData: boolean = false;  
  selectProduct: String;
  constructor() { 
    this.products = [
      "ทั้งหมด",
      "ห้างหุ้นส่วนจำกัด บริษัทจำกัดและบริษัทมหาชนจำกัด",    
      "การประกอบธุรกิจของคนต่างด้าว",
      "สมาคมและหอการค้า",       
    ];
  }

  ngOnInit() {
    $("#table1").DataTable()
  }

  
  searchData(): void {
    this.showData = true;
    $('#table1').DataTable( {
      order: [[ 1, "desc" ]]
  } );
  }
  
  clearData(): void {
    this.showData = false;
  }

  popupAddData() {
    $('#modal2').modal('show');
  }

  closePopupAdd() {
    $('#modal2').modal('hide');
  }

}
