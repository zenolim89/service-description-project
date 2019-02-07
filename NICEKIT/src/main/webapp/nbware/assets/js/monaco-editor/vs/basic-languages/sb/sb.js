/*
 * !-----------------------------------------------------------------------------
 * Copyright (c) Microsoft Corporation. All rights reserved. monaco-languages
 * version: 1.6.0(858705e74270e53559a241fdee187e7a6ae53b23) Released under the
 * MIT license
 * https://github.com/Microsoft/monaco-languages/blob/master/LICENSE.md
 * -----------------------------------------------------------------------------
 */
define("vs/basic-languages/sb/sb", [ "require", "exports" ], function(e, o) {
	"use strict";
	Object.defineProperty(o, "__esModule", {
		value : !0
	}), o.conf = {
			comments : {
				lineComment : "'"
			},
			brackets : [ [ "(", ")" ], [ "[", "]" ], [ "If", "EndIf" ], [ "While", "EndWhile" ],
						[ "For", "EndFor" ], [ "Sub", "EndSub" ] ],
			autoClosingPairs : [ {
					open : '"',
					close : '"',
					notIn : [ "string", "comment" ]
			}, {
					open : "(",
					close : ")",
					notIn : [ "string", "comment" ]
			}, {
					open : "[",
					close : "]",
					notIn : [ "string", "comment" ]
			} ]
	}, o.language = {
			defaultToken : "",
			tokenPostfix : ".sb",
			ignoreCase : !0,
			brackets : [ {
					token : "delimiter.array",
					open : "[",
					close : "]"
			}, {
					token : "delimiter.parenthesis",
					open : "(",
					close : ")"
			}, {
					token : "keyword.tag-if",
					open : "If",
					close : "EndIf"
			}, {
					token : "keyword.tag-while",
					open : "While",
					close : "EndWhile"
			}, {
					token : "keyword.tag-for",
					open : "For",
					close : "EndFor"
			}, {
					token : "keyword.tag-sub",
					open : "Sub",
					close : "EndSub"
			} ],
			keywords : [ "Else", "ElseIf", "EndFor", "EndIf", "EndSub", "EndWhile", "For", "Goto",
						"If", "Step", "Sub", "Then", "To", "While" ],
			tagwords : [ "If", "Sub", "While", "For" ],
			operators : [ ">", "<", "<>", "<=", ">=", "And", "Or", "+", "-", "*", "/", "=" ],
			identifier : /[a-zA-Z_][\w]*/,
			symbols : /[=><:+\-*\/%\.,]+/,
			escapes : /\\(?:[abfnrtv\\"']|x[0-9A-Fa-f]{1,4}|u[0-9A-Fa-f]{4}|U[0-9A-Fa-f]{8})/,
			tokenizer : {
					root : [ {
						include : "@whitespace"
					}, [ /(@identifier)(?=[.])/, "type" ], [ /@identifier/, {
						cases : {
								"@keywords" : {
									token : "keyword.$0"
								},
								"@operators" : "operator",
								"@default" : "variable.name"
						}
					} ], [ /([.])(@identifier)/, {
						cases : {
								$2 : [ "delimiter", "type.member" ],
								"@default" : ""
						}
					} ], [ /\d*\.\d+/, "number.float" ], [ /\d+/, "number" ],
								[ /[()\[\]]/, "@brackets" ], [ /@symbols/, {
									cases : {
											"@operators" : "operator",
											"@default" : "delimiter"
									}
								} ], [ /"([^"\\]|\\.)*$/, "string.invalid" ],
								[ /"/, "string", "@string" ] ],
					whitespace : [ [ /[ \t\r\n]+/, "" ], [ /(\').*$/, "comment" ] ],
					string : [ [ /[^\\"]+/, "string" ], [ /@escapes/, "string.escape" ],
								[ /\\./, "string.escape.invalid" ], [ /"C?/, "string", "@pop" ] ]
			}
	}
});