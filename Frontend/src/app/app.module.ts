import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { HttpClientModule, provideHttpClient, withFetch } from '@angular/common/http';
import { AppComponent } from './app.component';
import { UserFeedComponent } from './user-feed/user-feed.component';
import { RouterModule } from '@angular/router';
import { routes } from './app.routes';
import {MatDialogModule } from '@angular/material/dialog';
import { AddPostDialogComponent } from './add-post-dialog/add-post-dialog.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

@NgModule({
  declarations: [
    AddPostDialogComponent
  ],  
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatInputModule,
    RouterModule.forRoot(routes,  { onSameUrlNavigation: 'reload' }),
    MatDialogModule
  ],
  providers: [
    provideHttpClient(withFetch())
  ],
  bootstrap: [],
})
export class AppModule { }