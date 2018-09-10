import { Component, OnInit } from '@angular/core';
import { TextDateTH, formatter } from '../../../common/\u0E49helper/datepicker';
declare var $: any;
@Component({
  selector: 'app-monitoring',
  templateUrl: './monitoring.component.html',
  styleUrls: ['./monitoring.component.css']
})
export class MonitoringComponent implements OnInit {
  showData: boolean = false;
  actionStatus: string[];
  category: string[];
  selectcategory: string;
  selectactionStatus: string;
  modal: string[] = ['cause'];
  constructor() {
    this.category = [
      "ทั้งหมด",
      " DBD Job",
      "On Demand Job",
      "GL Job",
    ];
    this.actionStatus = [
      "ทั้งหมด",
      "สำเร็จ",
      "ไม่สำเร็จ"
    ];

  }

  ngOnInit() {
    $("#calendar1").calendar({
      maxDate: new Date(),
      type: "date",
      text: TextDateTH,
      formatter: formatter()
    });
    $("#calendar2").calendar({
      maxDate: new Date(),
      type: "date",
      text: TextDateTH,
      formatter: formatter()
    });


  }

  openModal(id) {
    $(`#${id}`).modal('show');
  }

  closeModal(id) {
    $(`#${id}`).modal('hide');
  }

  ngAfterViewInit() {

  }
  searchData(): void {
    this.showData = true;
    setTimeout(() => {
      $("#table").DataTable({
        scrollX: true,
        searching: false,
        orderable: false,
        "columnDefs": [{
          "targets": [4, 5],
          "orderable": false
        }]
      });
    });

  }

  clearData(): void {
    this.showData = false;
  }

  Onselectcategory = event => {
    this.selectcategory = this.category[event.target.value];
  };

  OnselectactionStatus = event => {
    this.selectactionStatus = this.actionStatus[event.target.value];
  };


}
