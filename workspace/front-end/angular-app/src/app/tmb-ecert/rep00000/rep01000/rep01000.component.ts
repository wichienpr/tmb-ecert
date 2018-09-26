import { Component, OnInit } from '@angular/core';
declare var $: any;

@Component({
  selector: 'app-rep01000',
  templateUrl: './rep01000.component.html',
  styleUrls: ['./rep01000.component.css']
})
export class Rep01000Component implements OnInit {
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
      type: "date"
      
  
    });
    $("#calendar2").calendar({    
      maxDate: new Date(),
      type: "date"
  
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
        searching :false,
        "columnDefs": [{
          "targets": [8,9,10,11,17,15,16],
          "orderable": false
        }]
      });
    }, 1000);
  }
  
  clearData(): void {
    this.showData = false;
  }

 
}
