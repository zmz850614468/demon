/*
 * Copyright (c) 2022 Huawei Device Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { ToManyWithJoinEntity } from './ToManyWithJoinEntity';
import { ToManyEntity } from './ToManyEntity';
import { ToOneEntity } from './ToOneEntity';
import { Property } from '../Property';

export class Entity {
  className: string; //类名
  dbName: string; //表名
  pkProperty: Property; //主键属性
  toOneRelations: Array<ToOneEntity>; //一对一集合
  toManyRelations: Array<ToManyEntity>; //一对多集合
  incomingToManyRelations: Array<ToManyEntity>; //一对多集合

  joinEntityTempList: Array<any> // 临时存储
}