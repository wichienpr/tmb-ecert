import { Component, OnInit } from '@angular/core';
declare var $: any;
@Component({
  selector: 'app-email-configuration',
  templateUrl: './email-configuration.component.html',
  styleUrls: ['./email-configuration.component.css']
})
export class EmailConfigurationComponent implements OnInit {
  showData: boolean = false;  

  action: string[];
  selectaction: string;

   modal: string[] = ['uploadfile'];
  constructor() { 
     this.action = [
    
      "All",
      "Active",
      "Inactive"
     
  ];
  ;

}
  ngOnInit() {   

  }


  ngAfterViewInit() {

  }
  searchData(): void {
    this.showData = true;
    setTimeout(() => {
      $("#table").DataTable({
        scrollX: true,
        searching: false,
        orderable : false,             
        "columnDefs": [{
          "targets": [3],
          "orderable": false
        }]
      });
    });

  }

  clearData(): void {
    this.showData = false;
  }

  Onselectaction = event => {
    this.selectaction = this.action[event.target.value];
  };
  
  openModal(id) {
    $(`#${id}`).modal('show');
  }

  closeModal(id) {
    $(`#${id}`).modal('hide');
  }

}
