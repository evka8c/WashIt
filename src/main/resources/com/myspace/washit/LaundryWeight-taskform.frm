{"id":"31fd17ec-623c-4ba0-8290-65081c3d4b45","name":"LaundryWeight-taskform.frm","model":{"taskName":"LaundryWeight","processId":"src.OrderCreation","name":"task","properties":[{"name":"OrderIn","typeInfo":{"type":"OBJECT","className":"com.myspace.washit.Order","multiple":false},"metaData":{"entries":[{"name":"field-readOnly","value":true}]}},{"name":"OrderOut","typeInfo":{"type":"OBJECT","className":"com.myspace.washit.Order","multiple":false},"metaData":{"entries":[{"name":"field-readOnly","value":false}]}}],"formModelType":"org.kie.workbench.common.forms.jbpm.model.authoring.task.TaskFormModel"},"fields":[{"nestedForm":"6657e6b9-78cf-4e59-84bd-6bb0a244b37a","container":"FIELD_SET","id":"field_5099","name":"OrderOut","label":"Set estimated laundry weight","required":false,"readOnly":false,"validateOnChange":true,"helpMessage":"","binding":"OrderOut","standaloneClassName":"com.myspace.washit.Order","code":"SubForm","serializedFieldClassName":"org.kie.workbench.common.forms.fields.shared.fieldTypes.relations.subForm.definition.SubFormFieldDefinition"},{"nestedForm":"7d9192a0-a5ac-42bd-96a3-1c8c8a868850","container":"FIELD_SET","id":"field_6401","name":"OrderIn","label":"Choose laundry type","required":false,"readOnly":true,"validateOnChange":true,"helpMessage":"","binding":"OrderIn","standaloneClassName":"com.myspace.washit.Order","code":"SubForm","serializedFieldClassName":"org.kie.workbench.common.forms.fields.shared.fieldTypes.relations.subForm.definition.SubFormFieldDefinition"}],"layoutTemplate":{"version":2,"style":"FLUID","layoutProperties":{},"rows":[{"height":"12","properties":{},"layoutColumns":[{"span":"12","height":"12","properties":{},"rows":[],"layoutComponents":[{"dragTypeName":"org.kie.workbench.common.forms.editor.client.editor.rendering.EditorFieldLayoutComponent","properties":{"field_id":"field_5099","form_id":"31fd17ec-623c-4ba0-8290-65081c3d4b45"}}]}]},{"height":"12","properties":{},"layoutColumns":[{"span":"12","height":"12","properties":{},"rows":[],"layoutComponents":[{"dragTypeName":"org.kie.workbench.common.forms.editor.client.editor.rendering.EditorFieldLayoutComponent","properties":{"field_id":"field_6401","form_id":"31fd17ec-623c-4ba0-8290-65081c3d4b45"}}]}]}]}}