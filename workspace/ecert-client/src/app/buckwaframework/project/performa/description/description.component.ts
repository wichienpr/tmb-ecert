import { Component, OnInit } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-description',
  templateUrl: './description.component.html',
})
export class DescriptionComponent implements OnInit {
  allowed: string[];
  show: 0;
  selectallowed: string;
  modal: string[] = ['desp', 'allowed', 'document'];

  constructor() {
    this.allowed = [
      "กรุณาเลือก",
      "ข้อมูลไม่ครบถ้วน",
      "จำนวนเงินไม่ถูกต้อง",
      "อื่นๆ",
    ];
  }

  ngOnInit() {
    $('.ui.dropdown').dropdown();
    $('.menu .item').tab();               
  }
  ngAfterViewInit() {   
     
  }

  callTable = () => {
    
    setTimeout(this.dataTable,20);
  }

  clickT = s => {
    console.log("onClick : ", s);
    this.show = s;
  }

  openModal(id) {
    $(`#${id}`).modal('show');
  }

  closeModal(id) {
    $(`#${id}`).modal('hide');
  }

  onSelectactionStatus = event => {
    this.selectallowed = this.allowed[event.target.value];
  };

  dataTable(): void {
     $("#table").DataTable({
        scrollX: true,
        searching: false,
        sScrollXInner: "100%",
        "columnDefs": [{
          "targets": 1,
          "orderable": false
        }, {
          "targets": 2,
          "orderable": false
        }]
      });
  }
}
