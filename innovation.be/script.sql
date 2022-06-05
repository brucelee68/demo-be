-- statisticInMonths is the user defined function that help we to fetch the counting value of each type
-- in each months (12 recent months)
-- the result will be displayed in the sample structure:
-- time year	type	    count
-- 11	2021	Idea	    1
-- 11	2021	Improvement	2
-- 11	2021	Innovation	2
-- 12	2021	NULL	0
-- 1	2022	Innovation	2

DROP FUNCTION if exists statisticInMonths;
CREATE
OR REPLACE FUNCTION statisticInMonths()
RETURNS TABLE
(month integer,
year integer,
type varchar(255),
count bigint)
as $$

BEGIN

Drop view if exists month_year;
CREATE
OR REPLACE VIEW month_year AS
SELECT extract(MONTH FROM CURRENT_DATE - (val * INTERVAL '1 month')) ::integer AS month,
			extract(YEAR FROM CURRENT_DATE - (val * INTERVAL '1 month'))::integer AS year
FROM (VALUES (0), (1), (2), (3), (4), (5), (6), (7), (8), (9), (10), (11)) AS tempValues(val);

RETURN QUERY
select m.month as month, m.year as year, t.name as type, count(i.id) as count
from (select * from innovation ti where ti.status <> 3) as i
    right join month_year m
on extract (MONTH FROM i.created_at) = m.month and extract (YEAR FROM i.created_at) = m.year
    left join type t on i.type_id = t.id
group by m.month, m.year, t.name
order by m.year asc, m.month asc;

END;
$$
LANGUAGE plpgsql;

-- statisticInMonths is the user defined function that help we to fetch the counting value of each type
-- in each quarter (4 recent quarters)
-- the result will be displayed in the sample structure:
-- time year	type	    count
-- 2	2021	NULL	    0
-- 3	2021	Idea	    2
-- 3	2021	Improvement	1
-- 3	2021	Innovation	2
-- 4	2021	Idea	    12
DROP FUNCTION if exists statisticInQuarters;
CREATE
OR REPLACE FUNCTION statisticInQuarters()
RETURNS TABLE
(quarter integer,
year integer,
type varchar(255),
count bigint)
as $$

BEGIN

Drop view if exists quarter_year;
CREATE
OR REPLACE VIEW quarter_year AS
SELECT extract(QUARTER FROM CURRENT_DATE - (val * INTERVAL '3 month'))::integer AS quarter, extract(YEAR FROM CURRENT_DATE - (val * INTERVAL '3 month')) ::integer AS year
FROM (VALUES (0), (1), (2), (3)) AS tempValues(val);

RETURN QUERY
select m.quarter as quarter, m.year as year, t.name as type, count(i.id) as count
from (select * from innovation ti where ti.status <> 3) as i
    right join quarter_year m
on extract (QUARTER FROM i.created_at) = m.quarter and extract (YEAR FROM i.created_at) = m.year
    left join type t on i.type_id = t.id
group by m.quarter, m.year, t.name
order by m.year asc, m.quarter asc;

END;
$$
LANGUAGE plpgsql;


-- statisticInMonths is the user defined function that help we to fetch the counting value of each type
-- in each quarter (4 recent quarters)
-- the result will be displayed in the sample structure:
-- time year	type	    count
-- 0	2021	Idea	    14
-- 0	2021	Improvement	11
-- 0	2021	Innovation	16
-- 0	2022	Innovation	2
DROP FUNCTION if exists statisticInYears;

CREATE
OR REPLACE FUNCTION statisticInYears()
RETURNS TABLE
(month integer,
year integer,
type varchar(255),
count bigint)
as $$

BEGIN

Drop view if exists list_year;
CREATE
OR REPLACE VIEW list_year AS
SELECT extract(YEAR FROM CURRENT_DATE - (val * INTERVAL '12 month')) ::integer AS year
FROM (VALUES (0), (1)) AS tempValues(val);

RETURN QUERY
select 0 as month, m.year as year, t.name as type, count(i.id) as count
from (select * from innovation ti where ti.status <> 3) as i
    right join list_year m
on extract (YEAR FROM i.created_at) = m.year
    left join type t on i.type_id = t.id
group by m.year, t.name
order by m.year asc;

END;
$$
LANGUAGE plpgsql;

-- topContributingProjectBetweenTwoDateInAllType is the user defined function that help we to fetch the counting value of
-- the top contributing project for innovation program in a certain time (provide as params) in all types
-- the result will be displayed in the sample structure:
-- name_value	    counting_final
-- FHM.C99.LMS2	    5
-- FHM.C99.KBBATC_2	5
-- FHM.C99.Flex_2	4
-- FHM.C99.RPP	    3
-- FHM.C99.ECA_2	2
-- Other	        12
DROP FUNCTION if exists topContributingProjectBetweenTwoDateInAllType;

CREATE
OR REPLACE FUNCTION topContributingProjectBetweenTwoDateInAllType(start_date date, end_date date, limit_value int)
RETURNS TABLE (name_value varchar(255), counting_final numeric)
LANGUAGE plpgsql
as $$
BEGIN

execute
    'create local temporary table top_contributors AS
    select te.project_name as project_name, count(i.id) as counting_value, ROW_NUMBER() OVER (ORDER BY (COUNT(i.id)) DESC) AS counter
    from innovation i inner join team te on i.team_id = te.id
    where i.created_at between $1 and $2 and i.status <> 3
    group by te.project_name
    order by count(i.id) desc;'
    using start_date, end_date;

return query
SELECT project_name as name_value, counting_value as counting_final
FROM top_contributors
WHERE counter <= limit_value
UNION ALL
SELECT 'Other', SUM(counting_value)
FROM top_contributors
WHERE counter > limit_value;

Drop table if exists top_contributors;
END;
$$;


-- topContributingProjectBetweenTwoDateInOneType is the user defined function that help we to fetch the counting value of
-- the top contributing project for innovation program in a certain time (provide as params) in a specific type
-- the result will be displayed in the sample structure:
-- name_value	    counting_final
-- FHM.C99.LMS2	    5
-- FHM.C99.KBBATC_2	5
-- FHM.C99.Flex_2	4
-- FHM.C99.RPP	    3
-- FHM.C99.ECA_2	2
-- Other	        12
DROP FUNCTION if exists topContributingProjectBetweenTwoDateInOneType;

CREATE
OR REPLACE FUNCTION topContributingProjectBetweenTwoDateInOneType(start_date date, end_date date, type_id bigint, limit_value int)
RETURNS TABLE (name_value varchar(255), counting_final numeric)
LANGUAGE plpgsql
as $$
BEGIN

execute
    'create local temporary table top_contributors_one_type AS
    select te.project_name as project_name, count(i.id) as counting_value, ROW_NUMBER() OVER (ORDER BY (COUNT(i.id)) DESC) AS counter
    from innovation i
    inner join team te on i.team_id = te.id
    where i.created_at between $1 and $2 and i.type_id = $3 and i.status <> 3
    group by te.project_name
    order by count(i.id) desc;'
    using start_date, end_date, type_id;

return query
SELECT project_name as name_value, counting_value as counting_final
FROM top_contributors_one_type
WHERE counter <= limit_value
UNION ALL
SELECT 'Other', SUM(counting_value)
FROM top_contributors_one_type
WHERE counter > limit_value;

Drop table if exists top_contributors_one_type;
END;
$$;

-- topTrendingAreaBetweenTwoDateInAllType is the user defined function that help we to fetch the counting value of
-- the top trending areas for innovation program in a certain time (provide as params) in all types
-- the result will be displayed in the sample structure:
-- name_value	        counting_final
-- Testing	            5
-- Production support	5
-- Soft Skills	        5
-- Process	            3
-- Delivery	            3
-- Other	            10
DROP FUNCTION if exists topTrendingAreaBetweenTwoDateInAllType;

CREATE
OR REPLACE FUNCTION topTrendingAreaBetweenTwoDateInAllType(start_date date, end_date date, limit_value int)
RETURNS TABLE (name_value varchar(255), counting_final numeric)
LANGUAGE plpgsql
as $$
BEGIN

execute
    'create local temporary table top_areas_all_types AS
    select a.name as area_name, count(i.id) as counting_value, ROW_NUMBER() OVER (ORDER BY (COUNT(i.id)) DESC) AS counter
    from innovation i
    inner join innovation_area ia on i.id = ia.innovation_id
    inner join area a on ia.area_id = a.id
    where i.created_at between $1 and $2 and i.status <> 3
    group by a.name
    order by count(i.id) desc;'
    using start_date, end_date;

return query
SELECT area_name as name_value, counting_value as counting_final
FROM top_areas_all_types
WHERE counter <= limit_value
UNION ALL
SELECT 'Other', SUM(counting_value)
FROM top_areas_all_types
WHERE counter > limit_value;

Drop table if exists top_areas_all_types;
END;
$$;


-- topTrendingAreaBetweenTwoDateInOneType is the user defined function that help we to fetch the counting value of
-- the top trending areas for innovation program in a certain time (provide as params) in a specific type
-- the result will be displayed in the sample structure:
-- name_value	        counting_final
-- Testing	            5
-- Production support	5
-- Soft Skills	        5
-- Process	            3
-- Delivery	            3
-- Other	            10
DROP FUNCTION if exists topTrendingAreaBetweenTwoDateInOneType;

CREATE
OR REPLACE FUNCTION topTrendingAreaBetweenTwoDateInOneType(start_date date, end_date date, type_id bigint, limit_value int)
RETURNS TABLE (name_value varchar(255), counting_final numeric)
LANGUAGE plpgsql
as $$
BEGIN

execute
    'create local temporary table top_areas_one_types AS
    select a.name as area_name, count(i.id) as counting_value, ROW_NUMBER() OVER (ORDER BY (COUNT(i.id)) DESC) AS counter
    from innovation i
    inner join innovation_area ia on i.id = ia.innovation_id
    inner join area a on ia.area_id = a.id
    where i.created_at between $1 and $2 and i.type_id = $3 and i.status <> 3
    group by a.name
    order by count(i.id) desc;'
    using start_date, end_date, type_id;

return query
SELECT area_name as name_value, counting_value as counting_final
FROM top_areas_one_types
WHERE counter <= limit_value
UNION ALL
SELECT 'Other', SUM(counting_value)
FROM top_areas_one_types
WHERE counter > limit_value;

Drop table if exists top_areas_one_types;
END;
$$;