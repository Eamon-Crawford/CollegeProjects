package com.example.nfcreader.parser


/*
 * Copyright (C) 2010 The Android Open Source Project
 * Modified by Sylvain Saurel for a tutorial
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import com.example.nfcreader.record.ParsedNdefRecord
import com.example.nfcreader.record.TextRecord
import java.util.*


object NdefMessageParser {
    fun parse(message: NdefMessage): List<ParsedNdefRecord> {
        return getRecords(message.records)
    }

    fun getRecords(records: Array<NdefRecord>): List<ParsedNdefRecord> {
        val elements: MutableList<ParsedNdefRecord> =
            ArrayList()
        for (record in records) {
            if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record))
            } else {
                elements.add(object : ParsedNdefRecord {
                    override fun str(): String? {
                        return String(record.payload)
                    }
                })
            }
        }
        return elements
    }
}