import { Component, OnInit } from '@angular/core';
import { TextDateTH } from '../../../common/\u0E49helper/datepicker';
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
    $("#calendar1").calendar({    
      maxDate: new Date(),
      type: "month",
      text: TextDateTH
  
    });
    $("#calendar2").calendar({    
      maxDate: new Date(),
      type: "month",
      text: TextDateTH
  
    });
  }

  ngAfterViewInit() {
    $("#table").DataTable();
  }

  onSelectProducts = event => {
    this.selectProduct = this.products[event.target.value];
  };
  

  searchData(): void {
    this.showData = true;
    setTimeout(() => {
      $("#table").DataTable({
        scrollX: true,
        searching :false
      });
    }, 200);
  }
  
  clearData(): void {
    this.showData = false;
  }

 
}
