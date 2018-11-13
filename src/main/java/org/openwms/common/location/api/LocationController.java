/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.openwms.common.location.api;

import org.ameba.exception.NotFoundException;
import org.ameba.mapping.BeanMapper;
import org.openwms.common.CommonConstants;
import org.openwms.common.location.Location;
import org.openwms.common.location.LocationPK;
import org.openwms.common.location.LocationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.lang.String.format;

/**
 * A LocationController.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
@RestController(CommonConstants.API_LOCATIONS)
class LocationController {

    private final LocationService locationService;
    private final BeanMapper mapper;

    LocationController(LocationService locationService, BeanMapper mapper) {
        this.locationService = locationService;
        this.mapper = mapper;
    }

    @GetMapping(params = {"locationPK"})
    LocationVO getLocation(@RequestParam("locationPK") String locationPk) {
        if (!LocationPK.isValid(locationPk)) {
            throw new NotFoundException(format("Invalid location [%s]", locationPk));
        }
        Location location = locationService.findByLocationId(LocationPK.fromString(locationPk));
        return mapper.map(location, LocationVO.class);
    }

    @GetMapping(params = {"locationGroupNames"})
    List<LocationVO> getLocationsForLocationGroup(@RequestParam("locationGroupNames") List<String> locationGroupNames) {
        List<Location> locations = locationService.findAllOf(locationGroupNames);
        return mapper.map(locations, LocationVO.class);
    }
}
