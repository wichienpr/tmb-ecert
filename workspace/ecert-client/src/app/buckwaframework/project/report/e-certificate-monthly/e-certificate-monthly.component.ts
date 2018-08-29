import { Component, OnInit } from '@angular/core';
import { TextDateTH } from '../../../common/\u0E49helper/datepicker';
declare var $: any;
@Component({
  selector: 'app-e-certificate-monthly',
  templateUrl: './e-certificate-monthly.component.html',
  styleUrls: ['./e-certificate-monthly.component.css']
})
export class ECertificateMonthlyComponent implements OnInit {
  showData: boolean = false; 
  constructor() { }

  ngOnInit() {
    $("#calendar1").calendar({    
      maxDate: new Date(),
      type: "month",
      text: TextDateTH
  
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
          "targets": 5,
          "orderable": false
        },{
          "targets": 6,
          "orderable": false
        },{
          "targets": 7,
          "orderable": false
        },{
          "targets": 10,
          "orderable": false
        }]
      });
    }, 200);
  }
  
  clearData(): void {
    this.showData = false;
  }

}
