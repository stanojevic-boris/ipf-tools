/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.tools.maven.dsldoc;

import java.util.Collections;
import java.util.List;

/**
 * Information about a documented DSL method.
 */
public class MethodInfo implements Comparable<MethodInfo> {
    private final String methodName;
    private final String comment;
    private final List<ParamInfo> paramInfos;
    private final String link;
    private final ParamInfo returnParam;

    /**
     * Constructs the DSL method info.
     * @param methodName
     *          the name of the method.
     * @param comment
     *          the JavaDoc comments of the method.
     * @param paramInfos
     *          the infos about the parameters of the method.
     * @param returnParam
     *          the info about the return parameter.
     * @param link
     *          the link for detailed documentation of the DSL method.
     */
    public MethodInfo(String methodName, String comment, List<ParamInfo> paramInfos, ParamInfo returnParam, String link) {
        this.methodName = methodName;
        this.comment = comment;
        this.paramInfos = Collections.unmodifiableList(paramInfos);
        this.link = link;
        this.returnParam = returnParam;
    }

    /**
     * @return the GDSL for the method.
     */
    public String toStringGDSL() {
        StringBuilder str = new StringBuilder();
        str.append("    method name: '");
        str.append(methodName);
        str.append("', type: '");
        str.append(returnParam.getTypeFull());
        str.append("'");
        if (!paramInfos.isEmpty()) {
            str.append(", params: [");
            boolean first = true;
            for (ParamInfo paramInfo : paramInfos) {
                if (!first) {
                    str.append(", ");
                }
                str.append(paramInfo.getName());
                str.append(": '");
                str.append(paramInfo.getTypeFull());
                str.append("'");
                first = false;
            }
            str.append("]");
        }
        return str.toString();
    }

    /**
     * @return the HTML documentation of the method.
     */
    public String toStringDSLIndex() {
        StringBuilder str = new StringBuilder();
        str.append("<tr class=\"b\"><td><tt>");
        appendParam(str, returnParam);
        str.append("</tt> <tt><a href=\"");
        str.append(link);
        str.append("\">");
        str.append(methodName);
        str.append("</a>(");
        boolean first = true;
        for (ParamInfo paramInfo : paramInfos) {
            if (!first) {
                str.append(", ");
            }
            appendParam(str, paramInfo);
            str.append(" ");
            str.append(paramInfo.getName());
            first = false;
        }
        str.append(")</tt><br>");
        str.append(comment);
        str.append("</td></tr>");
        
        return str.toString();
    }

    private void appendParam(StringBuilder str, ParamInfo paramInfo) {
        if (paramInfo.getLink().isEmpty()) {
            str.append(paramInfo.getType());
        }
        else {
            str.append("<a href=\"");
            str.append(paramInfo.getLink());
            str.append("\" title=\"");
            str.append(paramInfo.getTypeFull());
            str.append("\">");
            str.append(paramInfo.getType());
            str.append("</a>");
        }
    }

    public int compareTo(MethodInfo o) {
        return this.methodName.compareTo(o.methodName);
    }
}
