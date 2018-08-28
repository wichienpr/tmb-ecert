import { Component, OnInit } from '@angular/core';

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
  }

  
  searchData(): void {
    this.showData = true;
  }
  
  clearData(): void {
    this.showData = false;
  }

}
