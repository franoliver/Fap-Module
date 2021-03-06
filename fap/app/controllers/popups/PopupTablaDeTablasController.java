
package controllers.popups;

import play.mvc.Util;
import validation.CustomValidation;
import messages.Messages;
import models.TableKeyValue;
import controllers.gen.popups.PopupTablaDeTablasControllerGen;

public class PopupTablaDeTablasController extends PopupTablaDeTablasControllerGen {

	public static void crear(TableKeyValue tableKeyValue){
		checkAuthenticity();
		if(!permiso("create")){
			Messages.error("No tiene permisos suficientes para realizar la acción");
		}

		TableKeyValue dbtableKeyValue = new TableKeyValue();


		if(!Messages.hasErrors()){
			PopupTablaDeTablasValidateCopy(dbtableKeyValue, tableKeyValue);;
		}

		if(!Messages.hasErrors()){
			TableKeyValue.setValue(tableKeyValue);
		}

		if(!Messages.hasErrors()){
			renderJSON(utils.RestResponse.ok("Registro creado correctamente"));
		}else{
			Messages.keep();
			abrir("crear",null);
		}
	}



	public static void editar(Long idTableKeyValue,TableKeyValue tableKeyValue){
		checkAuthenticity();
		if(!permiso("update")){
			Messages.error("No tiene permisos suficientes para realizar la acción");
		}


		TableKeyValue dbtableKeyValue = null;
		if(!Messages.hasErrors()){
			dbtableKeyValue = getTableKeyValue(idTableKeyValue);
		}

		if(!Messages.hasErrors()){
			PopupTablaDeTablasValidateCopy(dbtableKeyValue, tableKeyValue);;
		}

		if(!Messages.hasErrors()){
			TableKeyValue.updateValue(dbtableKeyValue, tableKeyValue);
		}

		if(!Messages.hasErrors()){
			renderJSON(utils.RestResponse.ok("Registro actualizado correctamente"));
		}else{
			Messages.keep();
			abrir("editar",idTableKeyValue);
		}

	}



	public static void borrar(Long idTableKeyValue){
		checkAuthenticity();
		if(!permiso("delete")){
			Messages.error("No tiene permisos suficientes para realizar la acción");
		}

		TableKeyValue dbtableKeyValue = null;
		if(!Messages.hasErrors()){
			dbtableKeyValue = getTableKeyValue(idTableKeyValue);
		}

		if(!Messages.hasErrors()){
			TableKeyValue.removeValue(dbtableKeyValue);
		}

		if(!Messages.hasErrors()){
			renderJSON(utils.RestResponse.ok("Registro borrado correctamente"));
		}else{
			Messages.keep();
			abrir("borrar",idTableKeyValue);
		}
	}

	@Util
	protected static void PopupTablaDeTablasValidateCopy(TableKeyValue dbtableKeyValue, TableKeyValue tableKeyValue){
		CustomValidation.clearValidadas();
		CustomValidation.valid("tableKeyValue", tableKeyValue);
		CustomValidation.valid("tableKeyValue", tableKeyValue);
		CustomValidation.valid("tableKeyValue", tableKeyValue);
	}

}
