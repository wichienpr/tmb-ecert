import { Component, OnInit, OnDestroy } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-description',
  templateUrl: './description.component.html',
  styleUrls: ['./description.component.css']
})
export class DescriptionComponent implements OnInit, OnDestroy {
  allowed: string[];

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
    $('.menu .item')
      .tab();

    $("#table").DataTable({
      scrollX: true,
      searching: false,   
      bAutoWidth: true,  
      "columnDefs": [{
        "targets": 1,
        "orderable": false
      }]
    });
  }
  ngAfterViewInit() {
    $("#table").DataTable();
  }

  ngOnDestroy() {
    this.modal.forEach(element => {
      $(`#${element}`).remove();
    });
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

  searchData(): void {
    setTimeout(() => {
      $("#table").DataTable({
        scrollX: true,
        searching: false,
        "columnDefs": [{
          "targets": 1,
          "orderable": false
        }, {
          "targets": 2,
          "orderable": false
        }]
      });
    }, 200);
  }
}
