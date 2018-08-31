import { Component, OnInit } from '@angular/core';
import { TextDateTH, formatter } from '../../../common/\u0E49helper/datepicker';
declare var $: any;
@Component({
  selector: 'app-output-vat',
  templateUrl: './output-vat.component.html',
  styleUrls: ['./output-vat.component.css']
})
export class OutputVATComponent implements OnInit {
  showData: boolean = false; 
  constructor() { }

  ngOnInit() {
    $("#calendar1").calendar({    
      maxDate: new Date(),
      type: "month",      
      text: TextDateTH,
      formatter: formatter('month-year')
  
    });

  }
  ngAfterViewInit() {
    $("#table").DataTable();
  }
  

  searchData(): void {
    this.showData = true;
    setTimeout(() => {
      $("#table").DataTable({
        scrollX: true,
        searching :false,
        "columnDefs": [{
          "targets": 1,
          "orderable": false
        },{
          "targets": 2,
          "orderable": false
        },{
          "targets": 5,
          "orderable": false
        },{
          "targets": 6,
          "orderable": false
        },{
          "targets": 7,
          "orderable": false
        },{
          "targets": 8,
          "orderable": false
        },{
          "targets": 9,
          "orderable": false
        }]
      });
    }, 200);
  }
  
  
  clearData(): void {
    this.showData = false;
  }

}
