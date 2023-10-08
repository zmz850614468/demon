/*
 * Copyright (c) 2021 Huawei Device Co., Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

class Headers {
    private namesAndValues: string[];

    constructor(builder) {
        this.namesAndValues = builder.namesAndValues
    }

    static get Builder() {
        class Builder {
            namesAndValues = new Array();

            /** Add an header line containing a field name, a literal colon, and a value. */
            public add(line: string, value?: string | Date): Builder {
                let index = line.indexOf(":");
                if (index == -1) {
                    throw new Error("Unexpected header: " + line);
                } else if (value != null) {
                    if (value instanceof String) {
                        Headers.checkName(line.substring(0, index).trim());
                        Headers.checkValue(value as string, line.substring(0, index).trim());
                        this.addLenient(line.substring(0, index).trim(), value as string);
                    } else if (value instanceof Date) {
                        if (value == null) throw new Error("value for name " + line.substring(0, index).trim() + " == null");
                        Headers.checkName(line.substring(0, index).trim());
                        Headers.checkValue(new Date(value).toString(), line.substring(0, index).trim());
                        this.addLenient(line.substring(0, index).trim(), new Date(value).toString());
                    }
                }

                return this
            }

            addLenient(line: string, value?: string) {
                if (!!value && value != undefined) {
                    this.namesAndValues.push(line);
                    this.namesAndValues.push(value);
                    return this
                }
                let index = line.indexOf(":", 1);

                if (index != -1) {
                    this.namesAndValues.push(line.substring(0, index));
                    this.namesAndValues.push(line.substring(0, index + 1).trim());
                } else if (line.startsWith(":")) {
                    this.namesAndValues.push("");
                    this.namesAndValues.push(line.substring(1));
                } else {
                    this.namesAndValues.push("");
                    this.namesAndValues.push(line);
                }

                return this
            }

            public addUnsafeNonAscii(name: string, value: string) {
                Headers.checkName(name);
                return this.addLenient(name, value);
            }

            public addAll(headers: Headers) {
                for (var i = 0, size = headers.size(); i < size; i++) {
                    this.addLenient(headers.name(i), headers.value(i));
                }

                return this;
            }

            public set(name: string, value?: Date | string): Builder {
                if (value == null) throw new Error("value for name " + name + " == null");
                if (value != null && value != undefined) {
                    if (value instanceof String) {
                        Headers.checkName(name)
                        Headers.checkValue(value as string, name)
                        this.removeAll(name)
                        this.addLenient(name, value as string)
                    } else {
                        Headers.checkName(name)
                        Headers.checkValue(value as string, name)
                        this.removeAll(name)
                        this.addLenient(name, value as string)
                    }

                }

                return this

            }

            public removeAll(name: string): Builder {
                for (var i = 0; i < this.namesAndValues.length; i += 2) {
                    if (name.toLowerCase() == this.namesAndValues[i]) {
                        this.namesAndValues.splice(i, 1); // name
                        this.namesAndValues.splice(i, 1); // value
                        i -= 2;
                    }
                }
                return this
            }

            public build(): Headers {
                return new Headers(this);
            }
        }

        return Builder
    }

    static checkName(name: string): void {
        if (name == null) throw new Error("name == null");
        if (name == undefined) throw new Error("name is empty");
        for (var i = 0, length = name.length; i < length; i++) {
            let c = name.charAt(i)
            if (c <= '\u0020' || c >= '\u007f') {
                let msg = "Unexpected char " + escape(c) + "04x at " + i + " in header name: " + name
                throw new Error(escape(msg));
            }
        }
    }

    static checkValue(value: string, name: string) {
        if (value == null) throw new Error("value for name " + name + " == null");
        for (var i = 0, length = value.length; i < length; i++) {
            let c = value.charAt(i);
            if ((c <= '\u001f' && c != '\t') || c >= '\u007f') {
                let msg = "Unexpected char " + escape(c) + "04x at " + i + " in " + name + " value: " + value
                throw new Error(escape(msg));
            }
        }
    }

    addNamesAndValues(namesAndValues: string[]) {
        this.namesAndValues = namesAndValues;
    }

    public get(name: string) {
        for (var i = this.namesAndValues.length;i >= 0; i -= 2) {
            if (name.toLowerCase() == this.namesAndValues[i]) {
                return this.namesAndValues[i + 1];
            }
        }
        return null
    }

    public getDate(name: string) {
        let value = this.get(name)
        return value != null ? value.toLowerCase() : null;
    }

    public getInstant(name: string) {
        let value = this.getDate(name);
        return value != null ? value : null;
    }

    public size(): number {
        return this.namesAndValues.length / 2;
    }

    public name(index: number): string {
        return this.namesAndValues[index * 2];
    }

    public value(index: number): string {
        return this.namesAndValues[index * 2 + 1];
    }

    public names(): Array<string> {
        let result = new  Array<string>();
        for (var i = 0, size = this.size(); i < size; i++) {
            result.push(this.name(i));
        }
        return result
    }

    public values(name: string): Array<string> {
        let result = null;
        for (var i = 0, size = this.size(); i < size; i++) {
            if (name.toLowerCase() == this.name(i)) {
                if (result == null) {
                    result = new Array<string>(2);
                }
                result.add(this.value(i));
            }
        }
        return result != null ? result : null
    }

    public byteCount() {
        let result = this.namesAndValues.length * 2;
        for (var i = 0, size = this.namesAndValues.length; i < size; i++) {
            result += this.namesAndValues[i].length;
        }
        return result;
    }

    public newBuilder() {
        let result = new Headers.Builder();
        result.namesAndValues.concat(this.namesAndValues)
        return result
    }

    public toString() {
        let result = ''
        let size = this.size();
        for (var index = 0; index < size; index++) {
            result.concat(this.name(index)).concat(": ").concat(this.value(index)).concat('\n')
        }
        return result.toString();
    }

    public toMultimap(): Map<string, Array<string>> {
        let result = new Map<string, Array<string>>();
        let size = this.size()

        for (var index = 0; index < size; index++) {
            let name = this.name(index).toLowerCase()
            let values = result.get(name)
            if (values == null) {
                values = new Array(2)
                result.set(name, values)
            }
            values.push(this.value(index));
        }

        return result
    }
}

export default Headers