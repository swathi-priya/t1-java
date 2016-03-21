/*******************************************************************************
 * Copyright 2016 MediaMath
 * 
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
 ******************************************************************************/
package com.mediamath.terminalone.models.helper;

import javax.ws.rs.core.Form;

import com.mediamath.terminalone.Exceptions.T1Exception;
import com.mediamath.terminalone.Exceptions.ValidationException;
import com.mediamath.terminalone.models.StrategyConcept;
import com.mediamath.terminalone.models.StrategySupplySource;
import com.mediamath.terminalone.utils.Utility;

public class StrategySupplySourceHelper {

	
	public static void validateRequiredFields(StrategySupplySource entity) throws T1Exception {
		if (entity.getSupply_source_id() <= 0) {
			throw new ValidationException("please enter a valid Supply Source id");
		}
		
		if (entity.getStrategy_id() <= 0) {
			throw new ValidationException("please enter a valid strategy id");
		}
		
		/*if(entity.getVersion() <= 0) {
			throw new ValidationException("please add version");
		}*/
		
	}
	
	public static Form getForm(StrategySupplySource entity) {
		Form strategyConceptForm = new Form();
		
		if(entity.getSupply_source_id() > 0) {
			strategyConceptForm.param("supply_source_id", String.valueOf(entity.getSupply_source_id()));
		}

		if(entity.getStrategy_id() > 0) {
			strategyConceptForm.param("strategy_id", String.valueOf(entity.getStrategy_id()));
		}

		
		if(entity.getVersion() > 0) {
			strategyConceptForm.param("version", String.valueOf(entity.getVersion()));
		}
		
		return strategyConceptForm;
	}
	
	
	
	
}