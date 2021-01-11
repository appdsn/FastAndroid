/*
 * Copyright © Zhenjie Yan
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
package com.yanzhenjie.permission;

import android.content.Context;

/**
 * Created by Zhenjie Yan on 2016/9/10.
 */
public interface Rationale<T> {

    /**
     * Show rationale to user.
     *
     * @param context  context.
     * @param data     the data.
     * @param executor executor.
     */
    void showRationale(Context context, T data, RequestExecutor executor);
}