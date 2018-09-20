import { Component, OnInit } from '@angular/core';
import { TextDateTH, formatter } from 'helpers/index';
declare var $: any;
@Component({
  selector: 'app-monitoring',
  templateUrl: './monitoring.component.html',
  styleUrls: ['./monitoring.component.css']
})
export class MonitoringComponent implements OnInit {
  showData: boolean = false;
  actionStatus: any[];
  category: any[];
  selectcategory: any;
  selectactionStatus: any;
  modal: any[] = ['cause','rerun'];
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

  calendar=()=>{
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

    $("#modlecalendar1").calendar({
      maxDate: new Date(),
      type: "date",
      text: TextDateTH,
      formatter: formatter()
    });

    $("#modlecalendar2").calendar({
      maxDate: new Date(),
      type: "date",
      text: TextDateTH,
      formatter: formatter()
    });
  }
  ngOnInit() {
   
    this.calendar();

  }

  openModal(id) {
    $(`#${id}`).modal({
      autofocus : false,
      onShow : () =>{
        this.calendar();
        
      }

    }).modal('show');

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
