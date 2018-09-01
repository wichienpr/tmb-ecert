import { Component, OnInit, OnDestroy } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-description',
  templateUrl: './description.component.html',
  styleUrls: ['./description.component.css']
})
export class DescriptionComponent implements OnInit, OnDestroy {
  allowed: string[];
  show:0;
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
  clickT=s=>{
    console.log("onClick : ",s);       
  this.show =s;
    
  }
  ngOnInit() {
    $('.ui.dropdown').dropdown();    
      $("#table").DataTable({
        scrollX: true,
        searching: false,   
        bScrollCollapse: true,
        
        "columnDefs": [{
          "targets": 1,
          "orderable": false
        }]
      });

    
  }
  ngAfterViewInit() {
    $("#table").DataTable();
    $('.menu .item').tab();
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
