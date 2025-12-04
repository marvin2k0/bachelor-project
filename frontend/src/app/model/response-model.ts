import {PageModel} from './page-model';

export interface ResponseModel<T> {
  content: T,
  page: PageModel
}
