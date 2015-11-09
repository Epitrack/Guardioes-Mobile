//
//  Household.h
//  Guardioes da Saude
//
//  Created by Miqueias Lopes on 08/11/15.
//  Copyright © 2015 epitrack. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface Household : NSObject

- (id) initWithNick:(NSString *) nick andDob:(NSString *) dob andGender:(NSString *) gender andRace:(NSString *) race andIdUser:(NSString *) idUser andPicture:(NSString *) picture andIdHousehold:(NSString *) idHousehold;

@property(nonatomic, retain) NSString *nick;
@property(nonatomic, retain) NSString *dob;
@property(nonatomic, retain) NSString *gender;
@property(nonatomic, retain) NSString *race;
@property(nonatomic, retain) NSString *idUSer;
@property(nonatomic, retain) NSString *picture;
@property(nonatomic, retain) NSString *idHousehold;

@end
