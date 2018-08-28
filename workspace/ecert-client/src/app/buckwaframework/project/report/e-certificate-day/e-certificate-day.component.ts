import { Component, OnInit } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-e-certificate-day',
  templateUrl: './e-certificate-day.component.html',
  styleUrls: ['./e-certificate-day.component.css']
})
export class ECertificateDayComponent implements OnInit {
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
  }

  onSelectProducts = event => {
    this.selectProduct = this.products[event.target.value];
  };
  

  searchData(): void {
    this.showData = true;
  }
  
  clearData(): void {
    this.showData = false;
  }

 
}
