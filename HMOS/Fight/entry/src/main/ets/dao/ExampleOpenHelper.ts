/*
  * Copyright (c) 2022 Huawei Device Co., Ltd.
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
    *
  * http://www.apache.org/licenses/LICENSE-2.0
    *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

import {OpenHelper} from '@ohos/dataorm' ;
import {Database} from '@ohos/dataorm' ;

export class ExampleOpenHelper extends OpenHelper {

    constructor(context: any, name: string){
      super(context, name);
    }

    public onCreate_D(db: Database): void {
        super.onCreate_D(db);
    }

    //todo 自定义更新代码定义处
    onUpgrade_D(){

    }

}