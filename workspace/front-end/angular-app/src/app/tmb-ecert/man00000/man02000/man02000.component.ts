import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-man02000',
  templateUrl: './man02000.component.html',
  styleUrls: ['./man02000.component.css']
})
export class Man02000Component implements OnInit {
   pathVideo:String= "";
  constructor() {
    
   }

  ngOnInit() {
     this.pathVideo = "/ecert-webapp/api/manual/videoDownload";
  }
  openLink(){
    window.open("https://youtu.be/gMs-PgHeDF4", '_blank');
  }

}
