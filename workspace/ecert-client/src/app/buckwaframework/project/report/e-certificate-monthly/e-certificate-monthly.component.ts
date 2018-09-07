import { Component, OnInit } from '@angular/core';
import { formatter, TextDateTH } from '../../../common/\u0E49helper/datepicker';
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
      text: TextDateTH,
      formatter: formatter('month-year')

    });
    $("#calendar2").calendar({
      maxDate: new Date(),
      type: "month",
      text: TextDateTH,
      formatter: formatter('month-year')

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
        ordering: true,
        paging: true,
        "columnDefs": [{
          "targets": [2,3,4,5,7,8,9],
          "orderable": false
        }]
      });
    });

  }

  clearData(): void {
    this.showData = false;
  }

}
