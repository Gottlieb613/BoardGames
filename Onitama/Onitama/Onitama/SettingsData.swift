//
//  SettingsData.swift
//  Onitama
//
//  Created by Charlie Gottlieb on 6/30/24.
//

import Foundation
import Combine

class SharedSettings: ObservableObject {
    static let shared = SharedSettings()
    @Published var selectedGameMode: String = "2 Players" // Default value
    
    private init() {}
}
