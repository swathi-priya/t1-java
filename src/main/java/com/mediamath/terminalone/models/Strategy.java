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
package com.mediamath.terminalone.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Strategy implements T1Entity {
	
	private static final String entityName = "Strategy";
	
	public enum aud_seg_exc {
		AND, OR
	};

	public enum aud_seg_inc {
		AND, OR
	};

	public enum freq_int {
		hour("hour"), day("day"), week("week"), month("month"),campaign("campaign"), not_applicable("not-applcable");
		String val;
		freq_int(String s){
			val=s;
		}
	}; // should be not-applicable

	public enum freq_type {
		even("even"), asap("asap"), no_limit("no-limit");
		String val;
		freq_type(String s){
			val=s;
		}
	}; // should be no-limit


	public enum goal_type {
		spend, reach, cpc, cpe, cpa, roi
	};

	public enum media_type {
		DISPLAY, VIDEO
	};

	public enum pac_int {
		hour, day
	};

	public enum pac_type {
		even, asap
	};

	public enum site_select {
		MATHSELECT_250, EXCLUDE_UGC, ALL, REDUCED
	};

	public enum supply_type {
		RTB, RMX_API, T1_RMX
	};

	public enum type {
		REM, GBO, AUD
	};

	private aud_seg_exc audience_segment_exclude_op;
	private aud_seg_inc audience_segment_include_op;
	private float bid_aggresiveness;
	private boolean bid_price_is_media_only;
	private ArrayList<Currency> budget = new ArrayList<Currency>();
	private int campaign_id;
	private String created_on;
	private String description;
	private ArrayList<Currency> effective_goal_value = new ArrayList<Currency>();
	private Date end_date;
	private String feature_compatibility;
	private int frequency_amount;
	private freq_int frequency_interval;
	private freq_type frequency_type;
	private goal_type goal_type;
	private ArrayList<Currency> goal_value = new ArrayList<Currency>();
	private int id;
	private int impression_cap;
	private ArrayList<Currency> max_bid = new ArrayList<Currency>();
	private media_type media_type;
	private String name;
	private ArrayList<Currency> pacing_amount = new ArrayList<Currency>();
	private pac_int pacing_interval;
	private pac_type pacing_type;
	private ArrayList<Currency> roi_target = new ArrayList<Currency>();
	private String pixel_target_expr;
	private ArrayList<Integer> includePixels = new ArrayList<Integer>();
	private ArrayList<Integer> excludePixels = new ArrayList<Integer>();
	private boolean run_on_all_exchanges;
	private boolean run_on_all_pmp;
	private boolean run_on_display;
	private boolean run_on_mobile;
	private boolean run_on_streaming;
	private boolean site_restriction_transparent_urls;
	private site_select site_selectiveness;
	private Date start_date;
	private boolean status;
	private supply_type supply_type;
	private type type;
	private String updated_on;
	private boolean use_campaign_end;
	private boolean use_campaign_start;
	private boolean use_mm_freq;
	private boolean use_optimization;
	private int version;
	private String zone_name;
	private Aggregate aggregate;
	
	private Campaign campaign;
	
	List<StrategyDomain> strategy_domain_restrictions= new ArrayList<StrategyDomain>();
	List<Segments> audience_segments= new ArrayList<Segments>();
	List<TargetValues> target_values= new ArrayList<TargetValues>();
	List<Concept> concepts = new ArrayList<Concept>();

	public aud_seg_exc getAudience_segment_exclude_op() {
		return audience_segment_exclude_op;
	}

	public void setAudience_segment_exclude_op(aud_seg_exc audience_segment_exclude_op) {
		this.audience_segment_exclude_op = audience_segment_exclude_op;
	}

	public aud_seg_inc getAudience_segment_include_op() {
		return audience_segment_include_op;
	}

	public void setAudience_segment_include_op(aud_seg_inc audience_segment_include_op) {
		this.audience_segment_include_op = audience_segment_include_op;
	}

	public float getBid_aggresiveness() {
		return bid_aggresiveness;
	}

	public void setBid_aggresiveness(float bid_aggresiveness) {
		this.bid_aggresiveness = bid_aggresiveness;
	}

	public boolean isBid_price_is_media_only() {
		return bid_price_is_media_only;
	}

	public void setBid_price_is_media_only(boolean bid_price_is_media_only) {
		this.bid_price_is_media_only = bid_price_is_media_only;
	}

	public int getCampaign_id() {
		return campaign_id;
	}

	public void setCampaign_id(int campaign_id) {
		this.campaign_id = campaign_id;
	}

	public String getCreated_on() {
		return created_on;
	}

	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFeature_compatibility() {
		return feature_compatibility;
	}

	public void setFeature_compatibility(String feature_compatibility) {
		this.feature_compatibility = feature_compatibility;
	}

	public freq_int getFrequency_interval() {
		return frequency_interval;
	}

	public void setFrequency_interval(freq_int frequency_interval) {
		this.frequency_interval = frequency_interval;
	}

	public freq_type getFrequency_type() {
		return frequency_type;
	}

	public void setFrequency_type(freq_type frequency_type) {
		this.frequency_type = frequency_type;
	}

	public goal_type getGoal_type() {
		return goal_type;
	}

	public void setGoal_type(goal_type goal_type) {
		this.goal_type = goal_type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getImpression_cap() {
		return impression_cap;
	}

	public void setImpression_cap(int impression_cap) {
		this.impression_cap = impression_cap;
	}

	public media_type getMedia_type() {
		return media_type;
	}

	public void setMedia_type(media_type media_type) {
		this.media_type = media_type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public pac_int getPacing_interval() {
		return pacing_interval;
	}

	public void setPacing_interval(pac_int pacing_interval) {
		this.pacing_interval = pacing_interval;
	}

	public pac_type getPacing_type() {
		return pacing_type;
	}

	public void setPacing_type(pac_type pacing_type) {
		this.pacing_type = pacing_type;
	}

	public String getPixel_target_expr() {
		return pixel_target_expr;
	}

	public void setPixel_target_expr(String pixel_target_expr) {
		this.pixel_target_expr = pixel_target_expr;
	}

	public boolean isRun_on_all_exchanges() {
		return run_on_all_exchanges;
	}

	public void setRun_on_all_exchanges(boolean run_on_all_exchanges) {
		this.run_on_all_exchanges = run_on_all_exchanges;
	}

	public boolean isRun_on_all_pmp() {
		return run_on_all_pmp;
	}

	public void setRun_on_all_pmp(boolean run_on_all_pmp) {
		this.run_on_all_pmp = run_on_all_pmp;
	}

	public boolean isRun_on_display() {
		return run_on_display;
	}

	public void setRun_on_display(boolean run_on_display) {
		this.run_on_display = run_on_display;
	}

	public boolean isRun_on_mobile() {
		return run_on_mobile;
	}

	public void setRun_on_mobile(boolean run_on_mobile) {
		this.run_on_mobile = run_on_mobile;
	}

	public boolean isRun_on_streaming() {
		return run_on_streaming;
	}

	public void setRun_on_streaming(boolean run_on_streaming) {
		this.run_on_streaming = run_on_streaming;
	}

	public boolean isSite_restriction_transparent_urls() {
		return site_restriction_transparent_urls;
	}

	public void setSite_restriction_transparent_urls(boolean site_restriction_transparent_urls) {
		this.site_restriction_transparent_urls = site_restriction_transparent_urls;
	}

	public site_select getSite_selectiveness() {
		return site_selectiveness;
	}

	public void setSite_selectiveness(site_select site_selectiveness) {
		this.site_selectiveness = site_selectiveness;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public supply_type getSupply_type() {
		return supply_type;
	}

	public void setSupply_type(supply_type supply_type) {
		this.supply_type = supply_type;
	}

	public type getType() {
		return type;
	}

	public void setType(type type) {
		this.type = type;
	}

	public String getUpdated_on() {
		return updated_on;
	}

	public void setUpdated_on(String updated_on) {
		this.updated_on = updated_on;
	}

	public boolean isUse_campaign_end() {
		return use_campaign_end;
	}

	public void setUse_campaign_end(boolean use_campaign_end) {
		this.use_campaign_end = use_campaign_end;
	}

	public boolean isUse_campaign_start() {
		return use_campaign_start;
	}

	public void setUse_campaign_start(boolean use_campaign_start) {
		this.use_campaign_start = use_campaign_start;
	}

	public boolean isUse_mm_freq() {
		return use_mm_freq;
	}

	public void setUse_mm_freq(boolean use_mm_freq) {
		this.use_mm_freq = use_mm_freq;
	}

	public boolean isUse_optimization() {
		return use_optimization;
	}

	public void setUse_optimization(boolean use_optimization) {
		this.use_optimization = use_optimization;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getEntityname() {
		return entityName;
	}

	public String getZone_name() {
		return zone_name;
	}

	public void setZone_name(String zone_name) {
		this.zone_name = zone_name;
	}

	public List<StrategyDomain> getStrategy_domain_restrictions() {
		return strategy_domain_restrictions;
	}

	public void setStrategy_domain_restrictions(List<StrategyDomain> strategy_domain_restrictions) {
		this.strategy_domain_restrictions = strategy_domain_restrictions;
	}

	public List<Segments> getAudience_segments() {
		return audience_segments;
	}

	public void setAudience_segments(List<Segments> audience_segments) {
		this.audience_segments = audience_segments;
	}

	public List<TargetValues> getTarget_values() {
		return target_values;
	}

	public void setTarget_values(List<TargetValues> target_values) {
		this.target_values = target_values;
	}

	public Campaign getCampaign() {
		return campaign;
	}

	public void setCampaign(Campaign campaign) {
		this.campaign = campaign;
	}

	public List<Concept> getConcepts() {
		return concepts;
	}

	public void setConcepts(List<Concept> concepts) {
		this.concepts = concepts;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	public Date getStart_date() {
		return start_date;
	}

	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}

	public int getFrequency_amount() {
		return frequency_amount;
	}

	public void setFrequency_amount(int frequency_amount) {
		this.frequency_amount = frequency_amount;
	}

	///
	public ArrayList<Currency> getBudget() {
		return budget;
	}

	public void setBudget(ArrayList<Currency> budget) {
		this.budget = budget;
	}
	
	public void setBudget(float budget) {
		Currency currency = new Currency();
		currency.setValue(budget);
		this.budget.add(currency);
	}

	public ArrayList<Currency> getEffective_goal_value() {
		return effective_goal_value;
	}

	public void setEffective_goal_value(ArrayList<Currency> effective_goal_value) {
		this.effective_goal_value = effective_goal_value;
	}
	
	public void setEffective_goal_value(float effective_goal_value) {
		Currency currency = new Currency();
		currency.setValue(effective_goal_value);
		this.effective_goal_value.add(currency);
	}

	public ArrayList<Currency> getGoal_value() {
		return goal_value;
	}

	public void setGoal_value(ArrayList<Currency> goal_value) {
		this.goal_value = goal_value;
	}
	
	public void setGoal_value(float goal_value) {
		Currency currency = new Currency();
		currency.setValue(goal_value);
		this.goal_value.add(currency);
	}

	public ArrayList<Currency> getMax_bid() {
		return max_bid;
	}

	public void setMax_bid(ArrayList<Currency> max_bid) {
		this.max_bid = max_bid;
	}
	
	public void setMax_bid(float max_bid) {
		Currency currency = new Currency();
		currency.setValue(max_bid);
		this.max_bid.add(currency);
	}

	public ArrayList<Currency> getPacing_amount() {
		return pacing_amount;
	}

	public void setPacing_amount(ArrayList<Currency> pacing_amount) {
		this.pacing_amount = pacing_amount;
	}
	
	public void setPacing_amount(float pacing_amount) {
		Currency currency = new Currency();
		currency.setValue(pacing_amount);
		this.pacing_amount.add(currency);
	}

	public ArrayList<Currency> getRoi_target() {
		return roi_target;
	}

	public void setRoi_target(ArrayList<Currency> roi_target) {
		this.roi_target = roi_target;
	}

	public void setRoi_target(float roi_target) {
		Currency currency = new Currency();
		currency.setValue(roi_target);
		this.roi_target.add(currency);
	}

	public Aggregate getAggregate() {
		return aggregate;
	}

	public void setAggregate(Aggregate aggregate) {
		this.aggregate = aggregate;
	}

	public ArrayList<Integer> getIncludePixels() {
		return includePixels;
	}

	public void setIncludePixels(ArrayList<Integer> includePixels) {
		this.includePixels = includePixels;
	}
	
	public void setIncludePixels(Integer pixelId) {
		this.includePixels.add(pixelId);
	}

	public ArrayList<Integer> getExcludePixels() {
		return excludePixels;
	}

	public void setExcludePixels(ArrayList<Integer> excludePixels) {
		this.excludePixels = excludePixels;
	}
	
	public void setExcludePixels(Integer pixelId) {
		this.excludePixels.add(pixelId);
	}
	
	
}