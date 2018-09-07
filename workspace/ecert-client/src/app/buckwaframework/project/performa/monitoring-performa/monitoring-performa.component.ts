import { Component, OnInit } from '@angular/core';
import { TextDateTH, formatter } from '../../../common/\u0E49helper/datepicker';
declare var $: any;
@Component({
  selector: 'app-monitoring-performa',
  templateUrl: './monitoring-performa.component.html',
  styleUrls: ['./monitoring-performa.component.css']
})
export class MonitoringPerformaComponent implements OnInit {
  products: String[];
  showData: boolean = false;
  actionStatus: String[];
  selectProduct: String;
  selectactionStatus: String;
  constructor() {
    this.products = [
      "กรุณาเลือก",
      "ห้างหุ้นส่วนจำกัด บริษัทจำกัดและบริษัทมหาชนจำกัด",
      "การประกอบธุรกิจของคนต่างด้าว",
      "สมาคมและหอการค้า",
    ];
    this.actionStatus = [
      "กรุณาเลือก",
      "New",
      "Approve Payment",
      "Reiect Payment",
      "Waiting Payment",
      "Payment Complete",
      "Payment Incomplete",
      "Upload to ECM Complete",
      "Upload to ECM Incomplete",
      "Upload to e-Certificate Complete",
      "Upload to e-Certificate Incomplete",
      "Cancel",
      "Waiting Request Form"
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
  onSelectProducts = event => {
    this.selectProduct = this.products[event.target.value];
  };

  onSelectactionStatus = event => {
    this.selectProduct = this.products[event.target.value];
  };


  ngAfterViewInit() {
    $("#table").DataTable();
    $('.ui.sidebar')
      .sidebar({
        context: '.ui.grid.pushable'
      })
      .sidebar('setting', 'transition', 'push')
      .sidebar('toggle');
  }

  searchData(): void {
    this.showData = true;
    setTimeout(() => {
      $("#table").DataTable({
        scrollX: true,
        ordering: true,
        searching: false,
        "columnDefs": [{
          "targets": 10,
          "orderable": false
        }]


      });
    }, 200);
  }
  clearData(): void {
    this.showData = false;
  }

  onToggle() {
    $('.ui.sidebar')
      .sidebar({
        context: '.ui.grid.pushable'
      })
      .sidebar('setting', 'transition', 'push')
      .sidebar('toggle');
  }

}
